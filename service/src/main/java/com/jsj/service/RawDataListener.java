package com.jsj.service;

import com.jsj.api.BuyResultEnum;
import com.jsj.api.entity.BuyInformation;
import com.jsj.api.entity.SecKillRequest;
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
//    @Value("${spring.kafka.consumer.response-topic}")
//    String responseTopic;
    @Autowired
    KafkaTemplate kafkaTemplate;
    @Autowired
    SecKillService secKillService;

    /**
     * 读取kafka数据，并进行业务处理
     *
     * @param record
     * @throws IOException
     */
    @KafkaListener(topics = {"${spring.kafka.consumer.request-topic}"})
    public void listen(ConsumerRecord<?, ?> record, Acknowledgment ack) throws IOException {
        String value = (String) record.value();
        SecKillRequest request = serializer.deserialize(value, SecKillRequest.class);
        //手动ack
        ack.acknowledge();
        log.info("Get request: [{}]", request.toString());
        try {
            BuyInformation buyInformation = request.getBuyInformation();
            BuyResultEnum buyResultEnum = secKillService.handle(buyInformation.getUserId(), buyInformation.getProductId(),
                    buyInformation.getBuyNumber(), false);
            log.info("BuyResultEnum: [{}]", buyResultEnum.toString());
        } catch (Exception s) {
            log.error(s.toString());
        }
    }
}
