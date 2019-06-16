package com.jsj.service;

import com.jsj.api.entity.SecKillRequest;
import com.jsj.api.util.JSONSerializer;
import com.jsj.api.util.Serializer;
import com.jsj.mq.MQApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = MQApplication.class)
public class KafkaProducerTest {
    @Autowired
    KafkaTemplate kafkaTemplate;

    @Test
    public void testMsgProduct() throws Exception {
        SecKillRequest request;
        Serializer serializer = new JSONSerializer();
        for (int i = 1; i <= 500; i++) {
            request = new SecKillRequest(System.currentTimeMillis(), 1L, 1L, 1);
            byte[] buf = serializer.serialize(request);
            String msg = new String(buf);
            kafkaTemplate.send("result", msg);
            Thread.sleep(1000);
        }
    }

}
