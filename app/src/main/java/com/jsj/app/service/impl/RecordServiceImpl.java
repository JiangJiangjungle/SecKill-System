package com.jsj.app.service.impl;

import com.jsj.app.dao.RecordMapper;
import com.jsj.app.exception.DAOException;
import com.jsj.app.exception.ServiceException;
import com.jsj.app.pojo.entity.RecordDO;
import com.jsj.app.service.RecordService;
import com.jsj.app.util.KafkaUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.type.Alias;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Service
@Alias("recordService")
public class RecordServiceImpl implements RecordService {
    @Autowired
    private RecordMapper recordMapper;

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
        RecordDO recordDO = new RecordDO();
        recordDO.setCreateTime(new Date());
        recordDO.setProductId(productId);
        recordDO.setState(state);
        recordDO.setUserId(userId);
        try {
            kafkaUtils.send(recordDO);
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
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
        RecordDO recordDO = new RecordDO(userId, productId, state, new Date());
        try {
            return recordMapper.addRecord(recordDO);
        } catch (DAOException d) {
            throw new ServiceException("操作失败");
        }
    }

    @Override
    public RecordDO searchById(Integer id) throws ServiceException {
        if (null == id) {
            throw new ServiceException("id不能为空");
        }
        try {
            return recordMapper.getRecordById(id);
        } catch (DAOException d) {
            throw new ServiceException("操作失败");
        }
    }
}
