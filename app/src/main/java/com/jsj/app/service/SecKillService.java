package com.jsj.app.service;

import com.jsj.app.common.BuyResultEnum;
import com.jsj.app.exception.ServiceException;

public interface SecKillService {

    /**
     * 不通过缓存，直接进行秒杀，秒杀成功同时新增交易记录
     * @param userId 用户ID
     * @param productId 商品ID
     * @param buyNumber 秒杀数量
     * @param optimisticLock 是否采用乐观锁
     * @return
     * @throws ServiceException
     */
    BuyResultEnum handleWithoutRedis(String userId, String productId, int buyNumber, boolean optimisticLock) throws ServiceException;

    /**
     * 通过缓存预查询，再进行秒杀，秒杀成功同时新增交易记录
     * @param userId 用户ID
     * @param productId 商品ID
     * @param buyNumber 秒杀数量
     * @param optimisticLock 是否采用乐观锁
     * @return
     * @throws ServiceException
     */
    BuyResultEnum handleByRedis(String userId, String productId, int buyNumber, boolean optimisticLock) throws ServiceException;

    /**
     * 通过缓存预查询进行秒杀，交易记录通过Kafka异步处理
     * @param userId 用户ID
     * @param productId 商品ID
     * @param buyNumber 秒杀数量
     * @param optimisticLock 是否采用乐观锁
     * @return
     * @throws ServiceException
     */
    BuyResultEnum handleByRedisAndKafka(String userId, String productId, int buyNumber, boolean optimisticLock) throws ServiceException;


    /**
     * 通过缓存预查询，再通过获取分布式锁进行秒杀，交易记录通过Kafka异步处理
     * @param userId 用户ID
     * @param productId 商品ID
     * @param buyNumber 秒杀数量
     * @param optimisticLock 是否采用乐观锁
     * @param byRedisLock 是否采用redis锁（否则使用zookeeper锁）
     * @return
     * @throws ServiceException
     */
    BuyResultEnum handleByRedisAndKafkaAndDistributedLock(String userId, String productId, int buyNumber, boolean optimisticLock, boolean byRedisLock) throws ServiceException;

    /**
     * 在一个事务中，更新库存并新增交易记录
     * @param userId
     * @param productId
     * @param optimisticLock
     * @return
     * @throws ServiceException
     */
    boolean updateAndInsertRecord(String userId, String productId, boolean optimisticLock) throws ServiceException;

    /**
     * 在一个事务中，更新库存并发送交易记录到Kafka
     * @param userId
     * @param productId
     * @param optimisticLock
     * @return
     * @throws ServiceException
     */
    boolean updateAndSendMessage(String userId, String productId, boolean optimisticLock) throws ServiceException;

}
