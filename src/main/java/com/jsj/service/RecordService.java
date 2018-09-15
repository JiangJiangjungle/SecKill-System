package com.jsj.service;

import com.jsj.entity.RecordPO;
import com.jsj.exception.BaseException;

public interface RecordService {

    /**
     * 发送交易记录到消息队列
     * @param recordPO
     * @return
     * @throws BaseException
     */
    boolean sendRecordToMessageUtil(RecordPO recordPO) throws BaseException;

    /**
     * 直接新增交易记录
     *
     * @return
     * @throws BaseException
     */
    boolean addRecord(String userId,String productId,Integer state) throws BaseException;

    /**
     * 根据id查询交易记录
     * @param id
     * @return
     * @throws BaseException
     */
    RecordPO searchById(Integer id) throws BaseException;
}
