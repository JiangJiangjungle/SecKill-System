package com.jsj.dao;

import com.jsj.pojo.entity.RecordDO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RecordDAOTest {
    @Resource
    private RecordMapper recordMapper;

    @Test
    public void testAdd()throws Exception{
        UUID userId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        RecordDO recordDO = new RecordDO(null, userId.toString(), productId.toString(), 1, new Date());
        recordMapper.addRecord(recordDO);
    }

    @Test
    public void testDelete(){

    }

    @Test
    public void testUpdate(){

    }

    @Test
    public void testSearchByPrimaryId()throws Exception{
        int id = 1;
        RecordDO recordDO = recordMapper.getRecordById(id);
    }

    @Test
    public void testSearch()throws Exception{
        List<RecordDO> recordDOList = recordMapper.getAllRecords(0,1000);
        recordDOList.forEach(System.out::println);
    }
}
