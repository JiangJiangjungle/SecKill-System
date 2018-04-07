package com.jsj.mq;

import com.alibaba.fastjson.JSON;
import com.jsj.bean.Record;
import com.jsj.constant.ServiceRessult;
import com.jsj.mapper.PanicBuyingMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RabbitMqTest {

    @Resource
    RecordSender recordSender;

    @Autowired
    PanicBuyingMapper panicBuyingMapper;

    @Test
    public void test() {
        Record record = new Record(666, 999, ServiceRessult.SUCCESS.getValue(),
                ServiceRessult.SUCCESS.getLabel(), new Date());
        String message = JSON.toJSONString(record);
        recordSender.sendMsg(message);

    }


}
