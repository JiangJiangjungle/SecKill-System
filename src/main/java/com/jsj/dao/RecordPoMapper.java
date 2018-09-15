package com.jsj.dao;

import com.jsj.entity.RecordPO;
import com.jsj.exception.DAOException;

import java.util.List;

/**
 *
 * @author jiangshenjie
 * @date 2018-9-13
 */
public interface RecordPoMapper {

    boolean addRecord(RecordPO recordPO)throws DAOException;

    RecordPO getRecordById(Integer id)throws DAOException;

    List<RecordPO> getAllRecords()throws DAOException;
}
