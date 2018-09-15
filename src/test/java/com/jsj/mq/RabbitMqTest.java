package com.jsj.mq;

import com.alibaba.fastjson.JSON;
import com.jsj.entity.po.RecordPO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RabbitMqTest {

    @Resource
    RecordSender recordSender;

    @Test
    public void test() {
        RecordPO recordPO = new RecordPO();
        String message = JSON.toJSONString(recordPO);
        for (int i = 0; i < 10; i++) {
//            recordSender.sendMsg(i + "_msg");
        }
    }

    @Test
    public void test2() {

    }


}
