package com.jsj.dao;

import com.jsj.entity.po.RecordPO;

import java.util.List;

/**
 *
 * @author jiangshenjie
 * @date 2018-9-13
 */
public interface RecordPoMapper {

    boolean addRecord(RecordPO recordPO);

    RecordPO getRecordById(Integer id);

    List<RecordPO> getAllRecords();
}
