package com.jsj.mq;

import com.jsj.api.BuyResultEnum;
import com.jsj.api.entity.SecKillRequest;
import com.jsj.api.util.JSONSerializer;
import com.jsj.api.util.Serializer;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class RawDataListener {
    private Serializer serializer = new JSONSerializer();

    /**
     * 实时获取kafka数据，并进行业务处理
     *
     * @param record
     * @throws IOException
     */
    @KafkaListener(topics = {"${spring.kafka.consumer.response-topic}"})
    public void listen(ConsumerRecord<?, ?> record, Acknowledgment ack) throws IOException {
        String value = (String) record.value();
        BuyResultEnum buyResultEnum = serializer.deserialize(value, BuyResultEnum.class);
        log.info("Get buyResultEnum: " + buyResultEnum.toString());
        //todo 给客户端返回秒杀结果
        //提交
        ack.acknowledge();
    }
}
