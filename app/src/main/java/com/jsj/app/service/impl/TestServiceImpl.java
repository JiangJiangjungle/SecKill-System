package com.jsj.app.service.impl;

import com.jsj.app.common.RedisConfig;
import com.jsj.app.common.ZooKeeperConfig;
import com.jsj.app.common.BuyResultEnum;
import com.jsj.app.dao.ProductMapper;
import com.jsj.app.dao.RecordMapper;
import com.jsj.app.exception.DAOException;
import com.jsj.app.exception.ServiceException;
import com.jsj.app.lock.impl.RedisLock;
import com.jsj.app.lock.impl.ZookeeperLock;
import com.jsj.app.pojo.entity.RecordDO;
import com.jsj.app.service.RecordService;
import com.jsj.app.service.TestService;
import com.jsj.app.util.JedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;

import java.util.Date;
import java.util.concurrent.locks.Lock;

@Slf4j
@Service
public class TestServiceImpl implements TestService, ApplicationContextAware {

    @Autowired
    private JedisUtils jedisUtils;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private RecordMapper recordMapper;
    @Autowired
    private RedisConfig redisConfig;
    @Autowired
    private ZooKeeperConfig zooKeeperConfig;

    @Autowired
    private RecordService recordService;
    /**
     * 自己的代理对象
     */
    private TestService proxy;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.proxy = applicationContext.getBean(TestService.class);
    }

    @Transactional(rollbackFor = ServiceException.class)
    @Override
    public BuyResultEnum handleWithoutRedis(String userId, String productId, int buyNumber, boolean optimisticLock) throws ServiceException {
        boolean updated = this.proxy.updateAndInsertRecord(userId, productId, optimisticLock);
        if (updated) {
            return BuyResultEnum.SUCCESS;
        } else {
            log.info("库存不足，秒杀失败..userId: " + userId);
            return BuyResultEnum.FAIL;
        }
    }

    @Override
    public BuyResultEnum handleByRedis(String userId, String productId, int buyNumber, boolean optimisticLock) throws ServiceException {
        Jedis jedis = null;
        try {
            // 检查成功抢购名单中是否包含该用户
            jedis = jedisUtils.getJedis();
            if (jedis.sismember(productId, userId)) {
                // 返回重复秒杀信息
                return BuyResultEnum.REPEAT;
            }
            // 查询缓存中的库存数量
            int stock = chechStockByRedis(productId, jedis);
            boolean updated = false;
            if (stock > 0) {
                // 乐观锁更新数据库中的库存数量并新增交易记录
                updated = this.proxy.updateAndInsertRecord(userId, productId, optimisticLock);
            }
            // 若更新成功，则同时更新缓存
            if (updated) {
                log.info("商品id：{} ,用户id：{} 秒杀成功,数据库库存更新：{}", productId, userId, stock);
                this.updateUserList(userId, productId, jedis);
                return BuyResultEnum.SUCCESS;
            }
            log.info("乐观加锁失败或库存不足，秒杀失败..userId: " + userId);
            return BuyResultEnum.FAIL;
        } finally {
            jedisUtils.release(jedis);
        }
    }

    @Override
    public BuyResultEnum handleByRedisAndKafka(String userId, String productId, int buyNumber, boolean optimisticLock) throws ServiceException {
        Jedis jedis = null;
        try {
            // 检查成功抢购名单中是否包含该用户
            jedis = jedisUtils.getJedis();
            if (jedis.sismember(productId, userId)) {
                // 返回重复秒杀信息
                return BuyResultEnum.REPEAT;
            }
            // 查询缓存中的库存数量
            int stock = chechStockByRedis(productId, jedis);
            boolean updated = false;
            if (stock > 0) {
                // 乐观锁更新数据库中的库存数量并新增交易记录
                updated = this.proxy.updateAndSendMessage(userId, productId, optimisticLock);
            }
            // 若更新成功，则同时更新缓存
            if (updated) {
                log.info("商品id：{} ,用户id：{} 秒杀成功,数据库库存更新：{}", productId, userId, stock);
                this.updateUserList(userId, productId, jedis);
                return BuyResultEnum.SUCCESS;
            }
            log.info("乐观加锁失败或库存不足，秒杀失败..userId: " + userId);
            return BuyResultEnum.FAIL;
        } finally {
            jedisUtils.release(jedis);
        }
    }

    @Override
    public BuyResultEnum handleByRedisAndKafkaAndDistributedLock(String userId, String productId, int buyNumber, boolean optimisticLock, boolean byRedisLock) throws ServiceException {
        Jedis jedis = null;
        try {
            // 检查成功抢购名单中是否包含该用户
            jedis = jedisUtils.getJedis();
            if (jedis.sismember(productId, userId)) {
                // 返回重复秒杀信息
                return BuyResultEnum.REPEAT;
            }
            // 查询缓存中的库存数量
            int stock = chechStockByRedis(productId, jedis);
            boolean updated = false;
            if (stock > 0) {
                String threadID = String.valueOf(Thread.currentThread().getId());
                Lock lock = byRedisLock ?
                        new RedisLock(redisConfig.getRedisLockKey(), threadID, jedis) :
                        new ZookeeperLock(zooKeeperConfig.getHost(), zooKeeperConfig.getTimeout(),
                                zooKeeperConfig.getLockNameSpace(), zooKeeperConfig.getLockKey());
                try {
                    if (lock.tryLock()) {
                        // 更新数据库中的库存数量并生产交易记录消息
                        updated = this.proxy.updateAndSendMessage(userId, productId, optimisticLock);
                    }
                } finally {
                    lock.unlock();
                }
            }
            // 若更新成功，则同时更新缓存
            if (updated) {
                log.info("商品id：{} ,用户id：{} 秒杀成功,数据库库存更新：{}", productId, userId, stock);
                this.updateUserList(userId, productId, jedis);
                return BuyResultEnum.SUCCESS;
            }
            log.info("乐观加锁失败或库存不足，秒杀失败..userId: " + userId);
            return BuyResultEnum.FAIL;
        } finally {
            jedisUtils.release(jedis);
        }
    }

    @Override
    @Transactional(rollbackFor = ServiceException.class)
    public boolean updateAndInsertRecord(String userId, String productId, boolean optimisticLock) throws ServiceException {
        boolean updated;
        try {
            if (optimisticLock) {
                Integer versionId = productMapper.getVersionId(productId);
                updated = productMapper.updateStockByLock(productId, versionId);
            } else {
                updated = productMapper.updateStock(productId);
            }
            if (updated) {
                RecordDO recordDO = new RecordDO(userId, productId, BuyResultEnum.SUCCESS.getValue(), new Date());
                recordMapper.addRecord(recordDO);
            }
        } catch (DAOException d) {
            throw new ServiceException(d.getMessage());
        }
        return updated;
    }

    @Override
    @Transactional(rollbackFor = ServiceException.class)
    public boolean updateAndSendMessage(String userId, String productId, boolean optimisticLock) throws ServiceException {
        boolean updated;
        try {
            if (optimisticLock) {
                Integer versionId = productMapper.getVersionId(productId);
                updated = productMapper.updateStockByLock(productId, versionId);
            } else {
                updated = productMapper.updateStock(productId);
            }
        } catch (DAOException d) {
            throw new ServiceException(d.getMessage());
        }
        if (updated) {
            // 发送交易记录到消息队列
            recordService.sendRecordToMessageQueue(userId, productId, BuyResultEnum.SUCCESS.getValue());
            log.info("商品：" + productId + " ，用户：" + userId + " ，发送交易记录到消息队列");
        }
        return updated;
    }


    /**
     * 添加到抢购名单
     *
     * @param userId
     * @param productId
     * @param jedis
     * @throws ServiceException
     */
    private void updateUserList(String userId, String productId, Jedis jedis) {
        jedis.sadd(productId, userId);
        log.info("商品：" + productId + " ，用户：" + userId + " ，添加到成功抢购名单");
    }

    /**
     * 查询缓存中的库存数量
     *
     * @param productId
     * @param jedis
     * @return
     */
    private int chechStockByRedis(String productId, Jedis jedis) {
        String stockString = jedis.hget(redisConfig.getStockHashKey(), productId);
        return StringUtils.isEmpty(stockString) ? 0 : Integer.parseInt(stockString);
    }


    @Override
    public BuyResultEnum test(String userId, String productId, int buyNumber, boolean optimisticLock) throws ServiceException {
        Jedis jedis = null;
        try {
            // 检查成功抢购名单中是否包含该用户
            jedis = jedisUtils.getJedis();
            if (jedis.sismember(productId, userId)) {
                // 返回重复秒杀信息
                return BuyResultEnum.REPEAT;
            }
            // 查询缓存中的库存数量
            int stock = chechStockByRedis(productId, jedis);
            boolean updated = false;
            if (stock > 0) {
                // 乐观锁更新数据库中的库存数量并新增交易记录
                updated = this.proxy.updateAndSendMessage(userId, productId, optimisticLock);
                stock = productMapper.getStockById(productId);
                // 更新缓存中的库存数量
                jedis.hset(redisConfig.getStockHashKey(), productId, String.valueOf(stock));
            }
            log.info("商品id：{} ,redis的库存更新：{}", productId, stock);
            // 若更新成功，则同时更新缓存
            if (updated) {
                log.info("商品id：{} ,用户id：{} 秒杀成功,数据库库存更新：{}", productId, userId, stock);
                this.updateUserList(userId, productId, jedis);
                return BuyResultEnum.SUCCESS;
            }
            log.info("乐观加锁失败或库存不足，秒杀失败..userId: " + userId);
            return BuyResultEnum.FAIL;
        } catch (DAOException d) {
            throw new ServiceException(d.getMessage());
        } finally {
            jedisUtils.release(jedis);
        }
    }
}
