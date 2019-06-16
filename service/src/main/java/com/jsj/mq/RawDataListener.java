package com.jsj.mq;

import com.jsj.api.entity.SecKillRequest;
import com.jsj.api.util.JSONSerializer;
import com.jsj.api.util.Serializer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class RawDataListener {
    private Serializer serializer = new JSONSerializer();

    /**
     * 实时获取kafka数据(生产一条，监听生产topic自动消费一条)
     *
     * @param record
     * @throws IOException
     */
    @KafkaListener(topics = {"${spring.kafka.consumer.topic}"})
    public void listen(ConsumerRecord<?, ?> record) throws IOException {
        String value = (String) record.value();
        SecKillRequest request = serializer.deserialize(value, SecKillRequest.class);
        System.out.println("get request: " + request.toString());
    }
}
