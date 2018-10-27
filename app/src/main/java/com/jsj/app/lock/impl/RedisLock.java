package com.jsj.app.lock.impl;

import com.jsj.app.lock.AbstractLock;
import com.jsj.app.util.JedisUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author com.jsj
 * @date 2018-9-22
 */
@Slf4j
@Data
public class RedisLock extends AbstractLock {
    /**
     * 用于获取缓存连接
     */
    private JedisUtils jedisUtils;
    /**
     * 锁key值
     */
    private final String lockKey;
    /**
     * 锁value值:即线程id
     */
    private final String lockValue;
    /**
     * 是否持有锁
     */
    private volatile boolean locked = false;

    /**
     * 锁超时时间，防止线程在入锁以后，无限的执行等待，默认30000ms
     */
    private static final long EXPIRE_MILLISECOND = 30000;

    /**
     * 设置键的过期时间为 millisecond 毫秒
     */
    private static final String PX = "PX";

    /**
     * 只在键不存在时，才对键进行设置操作
     */
    private static final String NX = "NX";

    private static final String OK = "OK";

    public RedisLock(String lockKey, String lockValue, JedisUtils jedisUtils) {
        this.lockKey = lockKey;
        this.lockValue = lockValue;
        this.jedisUtils = jedisUtils;
    }

    @Override
    public boolean tryLock() {
        Jedis jedis = jedisUtils.getJedis();
        String result = jedis.set(lockKey, lockValue, NX, PX, EXPIRE_MILLISECOND);
        if (OK.equals(result)) {
            locked = true;
        }
        jedisUtils.release(jedis);
        return locked;
    }

    @Override
    public void unlock() {
        Jedis jedis = jedisUtils.getJedis();
        if (locked && this.lockValue.equals(jedis.get(lockKey))) {
            jedis.del(lockKey);
        }
        locked = false;
        jedisUtils.release(jedis);
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        long timeout = unit.toMillis(time);
        while (timeout >= 0) {
            if (this.tryLock()) {
                // 获得锁
                locked = true;
                return locked;
            }
            // 生成[10-200]区间的随机毫秒
            Random random = new Random();
            // randNumber 将被赋值为一个 MIN 和 MAX 范围内的随机数
            long delayMills = random.nextInt(100);
            timeout -= delayMills;
            log.debug("等待锁，锁key：{}，锁value：{}，等待时长：{}ms", lockKey, lockValue, delayMills);
            Thread.sleep(delayMills);
        }
        return locked;
    }
}
