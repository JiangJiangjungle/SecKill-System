package com.jsj.app.dao;

import com.jsj.app.exception.DAOException;
import com.jsj.app.pojo.entity.RecordDO;
import org.apache.ibatis.annotations.Param;
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

    RecordDO getRecordById(Integer id)throws DAOException;

    List<RecordDO> getAllRecords(@Param("start") int start, @Param("end") int end)throws DAOException;
}
