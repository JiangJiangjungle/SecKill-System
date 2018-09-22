package com.jsj.service;

import com.jsj.exception.ServiceException;
import com.jsj.constant.BuyResultEnum;

public interface PanicBuyService {

    /**
     * 利用Mysql乐观锁实现抢购
     * @param userId
     * @param productId
     * @param buyNumber
     * @return
     * @throws ServiceException
     */
    BuyResultEnum handleByOptimisticLock(String userId, String productId, int buyNumber) throws ServiceException;


    /**
     * 利用Mysql悲观锁实现抢购
     * @param userId
     * @param productId
     * @param buyNumber
     * @return
     * @throws ServiceException
     */
    BuyResultEnum handleByPessimisticLock(String userId, String productId, int buyNumber) throws ServiceException;

    /**
     * 查询商品库存
     * @param productId
     * @return
     * @throws ServiceException
     */
    int searchStock(String productId) throws ServiceException;
}