package com.jsj.dao;

import com.jsj.pojo.entity.RecordPO;
import com.jsj.exception.DAOException;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 * @author jiangshenjie
 * @date 2018-9-13
 */
public interface RecordPoMapper {

    boolean addRecord(RecordPO recordPO)throws DAOException;

    RecordPO getRecordById(Integer id)throws DAOException;

    List<RecordPO> getAllRecords(@Param("start")int start, @Param("end")int end)throws DAOException;
}
