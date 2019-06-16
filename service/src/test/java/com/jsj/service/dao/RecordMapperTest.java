package com.jsj.service.dao;


import com.jsj.api.entity.RecordDO;
import com.jsj.api.exception.DAOException;
import com.jsj.service.ServiceApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ServiceApplication.class)
public class RecordMapperTest {

    @Autowired
    RecordMapper recordMapper;

    @Test
    public void testAddRecord() throws DAOException {
        RecordDO recordDO = new RecordDO(1L, 1L, 0, new Date());
        boolean succeed = recordMapper.addRecord(recordDO);
    }

    @Test
    public void testRecordByPrimaryId() throws DAOException {
        RecordDO recordDO = recordMapper.getRecordByPrimaryId(1L);
        System.out.println(recordDO.toString());
    }

    @Test
    public void testAllRecords() throws DAOException {
        List<RecordDO> records = recordMapper.getAllRecords(0, 1000);
        for (RecordDO recordDO : records) {
            System.out.println(recordDO.toString());
        }
    }


}
