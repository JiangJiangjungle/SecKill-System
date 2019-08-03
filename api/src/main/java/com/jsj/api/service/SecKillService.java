package com.jsj.api.service;


import com.jsj.api.BuyResultEnum;
import com.jsj.api.exception.ServiceException;

/**
 * @author jiangshenjie
 */
public interface SecKillService {

    /**
     * 秒杀业务处理
     *
     * @param userId         用户ID
     * @param productId      商品ID
     * @param buyNumber      秒杀数量
     * @param optimisticLock 是否采用乐观锁
     * @return
     * @throws ServiceException
     */
    BuyResultEnum handle(Long userId, Long productId, Integer buyNumber, Boolean optimisticLock) throws ServiceException;

    /**
     * 在一个事务中，更新库存并新增交易记录
     *
     * @param userId
     * @param productId
     * @param optimisticLock
     * @return
     * @throws ServiceException
     */
    boolean decreaseStockAndAddRecord(Long userId, Long productId, Boolean optimisticLock) throws ServiceException;
}
