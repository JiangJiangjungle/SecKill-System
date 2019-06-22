package com.jsj.service;

import com.jsj.api.BuyResultEnum;
import com.jsj.api.entity.BuyInformation;
import com.jsj.api.entity.SecKillRequest;
import com.jsj.api.entity.SecKillResponse;
import com.jsj.api.service.SecKillService;
import com.jsj.api.util.JSONSerializer;
import com.jsj.api.util.Serializer;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author jiangshenjie
 */
@Slf4j
@Component
public class RawDataListener {
    private Serializer serializer = new JSONSerializer();
    @Value("${spring.kafka.consumer.response-topic}")
    String responseTopic;
    @Autowired
    KafkaTemplate kafkaTemplate;
    @Autowired
    SecKillService secKillService;

    /**
     * 实时获取kafka数据，并进行业务处理,再发送秒杀结果消息
     *
     * @param record
     * @throws IOException
     */
    @KafkaListener(topics = {"${spring.kafka.consumer.request-topic}"})
    public void listen(ConsumerRecord<?, ?> record, Acknowledgment ack) throws IOException {
        String value = (String) record.value();
        SecKillRequest request = serializer.deserialize(value, SecKillRequest.class);
        log.info("Get request: [{}]", request.toString());
        try {
            BuyInformation buyInformation = request.getBuyInformation();
            BuyResultEnum resultEnum = secKillService.handle(buyInformation.getUserId(), buyInformation.getProductId(),
                    buyInformation.getBuyNumber(), false);
            //同步发送秒杀结果消息
            SecKillResponse response = new SecKillResponse(System.currentTimeMillis(), buyInformation, resultEnum);
            kafkaTemplate.send(responseTopic, new String(serializer.serialize(response))).get();
            //请求消费手动提交
            ack.acknowledge();
        } catch (Exception s) {
            log.error(s.toString());
        }
    }
}
