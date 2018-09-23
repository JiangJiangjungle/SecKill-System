package com.jsj.lock.impl;

import com.jsj.lock.AbstractLock;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author jsj
 * @date 2018-9-23
 */
@Slf4j
public class ZookeeperLock extends AbstractLock {

    private ZooKeeper zooKeeper;

    /**
     * 锁根节点
     */
    private final String lockNamespace;
    /**
     * 锁值节点
     */
    private final String lockKey;
    /**
     * 当前节点
     */
    private String currentNode;
    /**
     * 等待的前一个节点
     */
    private String waitNode;
    /**
     * 竞争的节点列表
     */
    private List<String> lockNodes;
    /**
     * 计数器
     */
    private volatile CountDownLatch countDownLatch;
    /**
     * 是否持有锁
     */
    private volatile boolean locked = false;

    public ZookeeperLock(String address, int timeout, String lockNamespace, String lockKey) {
        this.init(address, timeout);
        this.lockNamespace = lockNamespace;
        this.lockKey = lockKey;
    }

    private void init(String address, int timeout) {
        try {
            zooKeeper = new ZooKeeper(address, timeout, watchedEvent ->
                    log.info("Zookeeper连接已建立, address: " + address)
            );
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public boolean tryLock() {
        String lock = lockNamespace + "/" + lockKey;
        try {
            // 确保zookeeper连接成功
            this.ensureZookeeperConnect();
            // 确保根节点存在
            ensureNameSpaceExist(lockNamespace);
            // 创建临时顺序节点
            // 节点目录为/xx/xx，节点为lockKey_xxx
            // currentNode值为lockKey_xxx
            currentNode = zooKeeper.create(lock, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE,
                    CreateMode.EPHEMERAL_SEQUENTIAL).replace(lockNamespace + "/", "");
            // 取出所有子节点
            List<String> childrenList = zooKeeper.getChildren(lockNamespace, false);
            // 竞争的节点列表
            lockNodes = new ArrayList<>();
            for (String children : childrenList) {
                if (children.startsWith(lockKey)) {
                    lockNodes.add(children);
                }
            }
            // 排序
            Collections.sort(lockNodes);
            // 如当前节点为最小节点，则成功获取锁
            if (currentNode.equals(lockNodes.get(0))) {
                locked = true;
            }
            return locked;
        } catch (InterruptedException | KeeperException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void unlock() {
        try {
            zooKeeper.delete(lockNamespace + "/" + currentNode, -1);
            zooKeeper.close();
            locked = false;
        } catch (InterruptedException | KeeperException e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * Zookeeper分布式锁
     * 实现思路：
     * 使用Zookeeper最小节点的方式
     * 执行过程：
     * 1、创建根节点，在根节点下创建顺序节点
     * 2、如当前创建的节点为根节点的所有子节点中最小的，则获取锁成功；
     * 否则，找到当前节点的前一个节点，watch前一个节点，当前一个节点被删除时获得锁；另外，等待超时也不能获得锁
     */
    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        //等待锁
        try {
            if (tryLock()) {
                return locked;
            }
            //找到当前节点的前一个节点
            waitNode = lockNodes.get(Collections.binarySearch(lockNodes, currentNode) - 1);
            this.waitLock(time, unit);
            return locked;
        } catch (KeeperException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 等待锁
     */
    private void waitLock(long time, TimeUnit unit) throws KeeperException, InterruptedException {
        String waitLock = lockNamespace + "/" + waitNode;
        log.info("等待锁 {} 释放", waitLock);
        Stat stat = zooKeeper.exists(waitLock, watchedEvent -> {
            if (countDownLatch != null) {
                locked = true;
                countDownLatch.countDown();
            }
        });
        // 前一个节点此刻存在，等待，节点消失则成功获取锁
        if (stat != null) {
            countDownLatch = new CountDownLatch(1);
            countDownLatch.await(time, unit);
            countDownLatch = null;
        } else {
            // 前一个节点此刻不存在，获得锁
            locked = true;
        }
    }

    /**
     * 确保根节点存在
     */
    private void ensureNameSpaceExist(String lockNamespace) throws KeeperException, InterruptedException {
        Stat statS = zooKeeper.exists(lockNamespace, false);
        if (statS == null) {
            //如果根节点不存在，创建
            zooKeeper.create(lockNamespace, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        }
    }

    /**
     * 确保zookeeper连接成功，防止出现连接还未完成就执行zookeeper的get/create/exsit操作出现错误KeeperErrorCode = ConnectionLoss
     */
    private void ensureZookeeperConnect() throws InterruptedException {
        CountDownLatch connectedLatch = new CountDownLatch(1);
        zooKeeper.register(watchedEvent -> {
            if (Watcher.Event.KeeperState.SyncConnected == watchedEvent.getState()) {
                connectedLatch.countDown();
            }
        });
        // zookeeper连接中则等待
        if (ZooKeeper.States.CONNECTING == zooKeeper.getState()) {
            connectedLatch.await();
        }
    }
}
