package com.jsj.mq.dao;


import com.jsj.api.entity.RecordDO;
import com.jsj.api.exception.DAOException;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *
 * @author jiangshenjie
 * @date 2018-9-13
 */
@Repository
public interface RecordMapper {

    boolean addRecord(RecordDO recordDO)throws DAOException;

    RecordDO getRecordByPrimaryId(Long id)throws DAOException;

    List<RecordDO> getAllRecords(Integer start, Integer end)throws DAOException;
}
