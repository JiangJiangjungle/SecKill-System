package com.jsj.api.dao;


import com.jsj.api.entity.RecordDO;
import com.jsj.api.exception.DAOException;

import java.util.List;

/**
 *
 * @author jiangshenjie
 * @date 2018-9-13
 */
public interface RecordMapper {

    boolean addRecord(RecordDO recordDO)throws DAOException;

    RecordDO getRecordByPrimaryId(Long id)throws DAOException;

    List<RecordDO> getAllRecords(Integer start, Integer end)throws DAOException;
}
