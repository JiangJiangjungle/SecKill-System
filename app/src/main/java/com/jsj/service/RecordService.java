package com.jsj.service;

import com.jsj.exception.ServiceException;
import com.jsj.pojo.entity.RecordDO;

/**
 * @author jiangshenjie
 * @date 2018-9-15
 */
public interface RecordService {

    /**
     * todo 发送交易记录到消息队列
     * @param userId
     * @param productId
     * @param state
     * @return
     * @throws ServiceException
     */
    void sendRecordToMessageQueue(String userId, String productId, Integer state) throws ServiceException;

    /**
     * 直接新增交易记录
     *
     * @return
     * @throws ServiceException
     */
    boolean addRecord(String userId, String productId, Integer state) throws ServiceException;

    /**
     * 根据id查询交易记录
     * @param id
     * @return
     * @throws ServiceException
     */
    RecordDO searchById(Integer id) throws ServiceException;
}
