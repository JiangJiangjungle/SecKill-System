package com.jsj.mq.service.impl;

import com.jsj.api.entity.RecordDO;
import com.jsj.api.exception.DAOException;
import com.jsj.api.exception.ServiceException;
import com.jsj.api.service.RecordService;
import com.jsj.mq.dao.RecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service("recordService")
public class RecordServiceImpl implements RecordService {
    @Autowired
    private RecordMapper recordMapper;

//    @Autowired
//    private KafkaUtils kafkaUtils;

    @Override
    public void sendRecordToMessageQueue(Long userId, Long productId, Integer state) throws ServiceException {
//        RecordDO recordDO = new RecordDO();
//        recordDO.setCreateTime(new Date());
//        recordDO.setProductId(productId);
//        recordDO.setState(state);
//        recordDO.setUserId(userId);
//        try {
//            kafkaUtils.send(recordDO);
//        } catch (Exception e) {
//            throw new ServiceException(e.getMessage());
//        }
    }

    @Override
    public boolean addRecord(Long userId, Long productId, Integer state) throws ServiceException {
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
    public RecordDO searchByPrimaryId(Long id) throws ServiceException {
        if (null == id) {
            throw new ServiceException("id不能为空");
        }
        try {
            return recordMapper.getRecordByPrimaryId(id);
        } catch (DAOException d) {
            throw new ServiceException("操作失败");
        }
    }
}
