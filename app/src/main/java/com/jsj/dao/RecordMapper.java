package com.jsj.dao;

import com.jsj.exception.DAOException;
import com.jsj.pojo.entity.RecordDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 * @author jiangshenjie
 * @date 2018-9-13
 */
public interface RecordMapper {

    boolean addRecord(RecordDO recordDO)throws DAOException;

    RecordDO getRecordById(Integer id)throws DAOException;

    List<RecordDO> getAllRecords(@Param("start") int start, @Param("end") int end)throws DAOException;
}
