package com.jsj.service;

import com.jsj.api.entity.RecordDO;
import com.jsj.api.service.RecordService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ServiceApplication.class)
public class RecordServiceTest {
    @Autowired
    RecordService recordService;

    @Test
    public void testAddRecord() throws Exception {
        boolean succeed = recordService.addRecord(1L,1L,0);
        System.out.println(succeed?"成功":"失败");
    }

    @Test
    public void testSearchByPrimaryId() throws Exception {
        RecordDO recordDO =recordService.searchByPrimaryId(1L);
        System.out.println(recordDO.toString());
    }

    @Test
    public void testSendRecordToMessageQueue() throws Exception {
//        recordService.sendRecordToMessageQueue(1L,1L,0);
//        System.out.println(recordDO.toString());
    }
}
