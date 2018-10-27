package com.jsj.app.service;

import com.jsj.app.constant.BuyResultEnum;
import com.jsj.app.exception.ServiceException;

public interface PanicBuyService {

    /**
     * 利用Mysql乐观锁实现抢购
     *
     * @param userId
     * @param productId
     * @param buyNumber
     * @return
     * @throws ServiceException
     */
    BuyResultEnum handleByOptimisticLock(String userId, String productId, int buyNumber) throws ServiceException;

    /**
     * todo 利用Mysql悲观锁实现抢购
     *
     * @param userId
     * @param productId
     * @param buyNumber
     * @return
     * @throws ServiceException
     */
    BuyResultEnum handleByPessimisticLock(String userId, String productId, int buyNumber) throws ServiceException;

    /**
     * 利用redis分布式锁实现抢购
     *
     * @param userId
     * @param productId
     * @param buyNumber
     * @return
     * @throws ServiceException
     */
    BuyResultEnum handleByRedisLock(String userId, String productId, int buyNumber) throws ServiceException;

    /**
     * todo 利用zookeeper分布式锁实现抢购
     *
     * @param userId
     * @param productId
     * @param buyNumber
     * @return
     * @throws ServiceException
     */
    BuyResultEnum handleByZookeeperLock(String userId, String productId, int buyNumber) throws ServiceException;

    /**
     * 查询主数据库的商品库存
     *
     * @param productId
     * @return
     * @throws ServiceException
     */
    int searchStock(String productId) throws ServiceException;
}