package com.jsj.service.impl;

import com.jsj.dao.RecordPoMapper;
import com.jsj.pojo.ServiceResult;
import com.jsj.pojo.entity.RecordPO;
import com.jsj.exception.DAOException;
import com.jsj.exception.ServiceException;
import com.jsj.service.RecordService;
import com.jsj.util.KafkaUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.type.Alias;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Service
@Alias("recordService")
public class RecordServiceImpl implements RecordService {
    @Resource
    private RecordPoMapper recordPoMapper;

    @Autowired
    private KafkaUtils kafkaUtils;

    @Override
    public void sendRecordToMessageQueue(String userId, String productId, Integer state) throws ServiceException {
        if (StringUtils.isEmpty(userId)) {
            throw new ServiceException("userId不能为空");
        }
        if (StringUtils.isEmpty(productId)) {
            throw new ServiceException("productId不能为空");
        }
        if (null == state) {
            throw new ServiceException("state不能为空");
        }
        RecordPO recordPO = new RecordPO();
        recordPO.setCreateTime(new Date());
        recordPO.setProductId(productId);
        recordPO.setState(state);
        recordPO.setUserId(userId);
        kafkaUtils.send(recordPO);
    }

    @Override
    public boolean addRecord(String userId, String productId, Integer state) throws ServiceException {
        if (StringUtils.isEmpty(userId)) {
            throw new ServiceException("userId不能为空");
        }
        if (StringUtils.isEmpty(productId)) {
            throw new ServiceException("productId不能为空");
        }
        if (null == state) {
            throw new ServiceException("state不能为空");
        }
        RecordPO recordPO = new RecordPO(userId, productId, state, new Date());
        try {
            return recordPoMapper.addRecord(recordPO);
        } catch (DAOException d) {
            throw new ServiceException("操作失败");
        }
    }

    @Override
    public RecordPO searchById(Integer id) throws ServiceException {
        if (null == id) {
            throw new ServiceException("id不能为空");
        }
        try {
            return recordPoMapper.getRecordById(id);
        } catch (DAOException d) {
            throw new ServiceException("操作失败");
        }
    }
}
