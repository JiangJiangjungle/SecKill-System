package com.jsj.canal.dao;

import com.jsj.api.entity.RecordDO;
import com.jsj.api.exception.DAOException;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author jiangshenjie
 * @date 2018-9-13
 */
@Repository
public interface RecordMapper {

    List<RecordDO> getAllRecords(@Param("start") int start, @Param("end") int end) throws DAOException;
}
