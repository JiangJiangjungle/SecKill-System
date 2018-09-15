package com.jsj.service.impl;

import com.jsj.dao.RecordPoMapper;
import com.jsj.entity.RecordPO;
import com.jsj.exception.BaseException;
import com.jsj.service.RecordService;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.type.Alias;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Service
@Alias("recordService")
public class RecordServiceImpl implements RecordService {
    @Resource
    private RecordPoMapper recordPoMapper;

    @Override
    public boolean sendRecordToMessageUtil(RecordPO recordPO) throws BaseException {
        return false;
    }

    @Override
    public boolean addRecord(String userId, String productId, Integer state) throws BaseException {
        if (StringUtils.isEmpty(userId)) {
            throw new BaseException("userId不能为空");
        }
        if (StringUtils.isEmpty(productId)) {
            throw new BaseException("productId不能为空");
        }
        if (null == state) {
            throw new BaseException("state不能为空");
        }
        RecordPO recordPO = new RecordPO(userId, productId, state,new Date());
        return recordPoMapper.addRecord(recordPO);
    }

    @Override
    public RecordPO searchById(Integer id) throws BaseException {
        if (null == id) {
            throw new BaseException("id不能为空");
        }
        return recordPoMapper.getRecordById(id);
    }
}
