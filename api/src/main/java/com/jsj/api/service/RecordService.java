package com.jsj.api.service;


import com.jsj.api.entity.RecordDO;
import com.jsj.api.exception.ServiceException;

/**
 * @author jiangshenjie
 * @date 2018-9-15
 */
public interface RecordService {

    /**
     * 直接新增交易记录
     *
     * @return
     * @throws ServiceException
     */
    boolean addRecord(Long userId, Long productId, Integer state) throws ServiceException;

    /**
     * 根据id查询交易记录
     * @param id
     * @return
     * @throws ServiceException
     */
    RecordDO searchByPrimaryId(Long id) throws ServiceException;
}
