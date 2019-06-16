package com.jsj.service.service.impl;

import com.jsj.api.BuyResultEnum;
import com.jsj.api.entity.RecordDO;
import com.jsj.api.exception.DAOException;
import com.jsj.api.exception.ServiceException;
import com.jsj.api.service.SecKillService;
import com.jsj.service.dao.ProductMapper;
import com.jsj.service.dao.RecordMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.Date;

@Slf4j
@Service("secKillService")
public class SecKillServiceImpl implements SecKillService, ApplicationContextAware {
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private RecordMapper recordMapper;
//    @Autowired
//    private RedisConfig redisConfig;
//    @Autowired
//    private ZooKeeperConfig zooKeeperConfig;
//
//    @Autowired
//    private RecordService recordService;
    /**
     * 自己的代理对象
     */
    private SecKillService proxy;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.proxy = applicationContext.getBean(SecKillService.class);
    }

    //
//    @Transactional(rollbackFor = ServiceException.class)
//    @Override
//    public BuyResultEnum handleWithoutRedis(String userId, String productId, int buyNumber, boolean optimisticLock) throws ServiceException {
//        boolean updated = this.proxy.updateAndInsertRecord(userId, productId, optimisticLock);
//        if (updated) {
//            return BuyResultEnum.SUCCESS;
//        } else {
//            log.info("库存不足，秒杀失败..userId: " + userId);
//            return BuyResultEnum.FAIL;
//        }
//    }
//
//    @Override
//    public BuyResultEnum handleByRedis(String userId, String productId, int buyNumber, boolean optimisticLock) throws ServiceException {
//        Jedis jedis = null;
//        try {
//            // 检查成功抢购名单中是否包含该用户
//            jedis = jedisUtils.getJedis();
//            if (jedis.sismember(productId, userId)) {
//                // 返回重复秒杀信息
//                return BuyResultEnum.REPEAT;
//            }
//            // 查询缓存中的库存数量
//            int stock = chechStockByRedis(productId, jedis);
//            boolean updated = false;
//            if (stock > 0) {
//                // 乐观锁更新数据库中的库存数量并新增交易记录
//                updated = this.proxy.updateAndInsertRecord(userId, productId, optimisticLock);
//            }
//            // 若更新成功，则同时更新缓存
//            if (updated) {
//                log.info("商品id：{} ,用户id：{} 秒杀成功,数据库库存更新：{}", productId, userId, stock);
//                this.updateUserList(userId, productId, jedis);
//                return BuyResultEnum.SUCCESS;
//            }
//            log.info("乐观加锁失败或库存不足，秒杀失败..userId: " + userId);
//            return BuyResultEnum.FAIL;
//        } finally {
//            jedisUtils.release(jedis);
//        }
//    }
//
//    @Override
//    public BuyResultEnum handleByRedisAndKafka(String userId, String productId, int buyNumber, boolean optimisticLock) throws ServiceException {
//        Jedis jedis = null;
//        try {
//            // 检查成功抢购名单中是否包含该用户
//            jedis = jedisUtils.getJedis();
//            if (jedis.sismember(productId, userId)) {
//                // 返回重复秒杀信息
//                return BuyResultEnum.REPEAT;
//            }
//            // 查询缓存中的库存数量
//            int stock = chechStockByRedis(productId, jedis);
//            boolean updated = false;
//            if (stock > 0) {
//                // 乐观锁更新数据库中的库存数量并新增交易记录
//                updated = this.proxy.updateAndSendMessage(userId, productId, optimisticLock);
//            }
//            // 若更新成功，则同时更新缓存
//            if (updated) {
//                log.info("商品id：{} ,用户id：{} 秒杀成功,数据库库存更新：{}", productId, userId, stock);
//                this.updateUserList(userId, productId, jedis);
//                return BuyResultEnum.SUCCESS;
//            }
//            log.info("乐观加锁失败或库存不足，秒杀失败..userId: " + userId);
//            return BuyResultEnum.FAIL;
//        } finally {
//            jedisUtils.release(jedis);
//        }
//    }
//
//    @Override
//    public BuyResultEnum handleByRedisAndKafkaAndDistributedLock(String userId, String productId, int buyNumber, boolean optimisticLock, boolean byRedisLock) throws ServiceException {
//        Jedis jedis = null;
//        try {
//            // 检查成功抢购名单中是否包含该用户
//            jedis = jedisUtils.getJedis();
//            if (jedis.sismember(productId, userId)) {
//                // 返回重复秒杀信息
//                return BuyResultEnum.REPEAT;
//            }
//            // 查询缓存中的库存数量
//            int stock = chechStockByRedis(productId, jedis);
//            boolean updated = false;
//            if (stock > 0) {
//                String threadID = String.valueOf(Thread.currentThread().getId());
//                Lock lock = byRedisLock ?
//                        new RedisLock(redisConfig.getRedisLockKey(), threadID, jedis) :
//                        new ZookeeperLock(zooKeeperConfig.getHost(), zooKeeperConfig.getTimeout(),
//                                zooKeeperConfig.getLockNameSpace(), zooKeeperConfig.getLockKey());
//                try {
//                    if (lock.tryLock()) {
//                        // 更新数据库中的库存数量并生产交易记录消息
//                        updated = this.proxy.updateAndSendMessage(userId, productId, optimisticLock);
//                    }
//                } finally {
//                    lock.unlock();
//                }
//            }
//            // 若更新成功，则同时更新缓存
//            if (updated) {
//                log.info("商品id：{} ,用户id：{} 秒杀成功,数据库库存更新：{}", productId, userId, stock);
//                this.updateUserList(userId, productId, jedis);
//                return BuyResultEnum.SUCCESS;
//            }
//            log.info("乐观加锁失败或库存不足，秒杀失败..userId: " + userId);
//            return BuyResultEnum.FAIL;
//        } finally {
//            jedisUtils.release(jedis);
//        }
//    }
//

    @Override
    public boolean decreaseStockAndAddRecord(Long userId, Long productId, Boolean optimisticLock) throws ServiceException {
        boolean updated;
        try {
            if (optimisticLock) {
                Integer versionId = productMapper.getVersionIdByPrimaryId(productId);
                updated = productMapper.decreaseStockByVersionId(productId, versionId);
            } else {
                updated = productMapper.decreaseStock(productId);
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

    //
//    @Override
//    @Transactional(rollbackFor = ServiceException.class)
//    public boolean updateAndSendMessage(String userId, String productId, boolean optimisticLock) throws ServiceException {
//        boolean updated;
//        try {
//            if (optimisticLock) {
//                Integer versionId = productMapper.getVersionId(productId);
//                updated = productMapper.updateStockByLock(productId, versionId);
//            } else {
//                updated = productMapper.updateStock(productId);
//            }
//        } catch (DAOException d) {
//            throw new ServiceException(d.getMessage());
//        }
//        if (updated) {
//            // 发送交易记录到消息队列
//            recordService.sendRecordToMessageQueue(userId, productId, BuyResultEnum.SUCCESS.getValue());
//            log.info("商品：" + productId + " ，用户：" + userId + " ，发送交易记录到消息队列");
//        }
//        return updated;
//    }
//
//
//    /**
//     * 添加到抢购名单
//     *
//     * @param userId
//     * @param productId
//     * @param jedis
//     * @throws ServiceException
//     */
//    private void updateUserList(String userId, String productId, Jedis jedis) {
//        jedis.sadd(productId, userId);
//        log.info("商品：" + productId + " ，用户：" + userId + " ，添加到成功抢购名单");
//    }
//
//    /**
//     * 查询缓存中的库存数量
//     *
//     * @param productId
//     * @param jedis
//     * @return
//     */
//    private int chechStockByRedis(String productId, Jedis jedis) {
//        String stockString = jedis.hget(redisConfig.getStockHashKey(), productId);
//        return StringUtils.isEmpty(stockString) ? 0 : Integer.parseInt(stockString);
//    }
//

    @Override
    public BuyResultEnum handle(Long userId, Long productId, Integer buyNumber, Boolean optimisticLock) throws ServiceException {
        boolean updated = this.proxy.decreaseStockAndAddRecord(userId, productId, optimisticLock);
        BuyResultEnum resultEnum = updated ? BuyResultEnum.SUCCESS : BuyResultEnum.FAIL;
        log.info("Sec-Kill result：[{}]，userId: [{}]", resultEnum.getLabel(), userId);
        return resultEnum;
    }

    @Override
    public BuyResultEnum handleByCache(Long userId, Long productId, Integer buyNumber, Boolean optimisticLock) throws ServiceException {
        return null;
    }
}
