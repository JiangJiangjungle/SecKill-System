package com.jsj.util.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.jsj.dao.RecordMapper;
import com.jsj.exception.DAOException;
import com.jsj.pojo.entity.RecordDO;
import com.jsj.util.KafkaUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.type.Alias;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Optional;

@Slf4j
@Component
@Alias("kafkaUtils")
public class KafkaUtilsImpl implements KafkaUtils {

    private final String TOPIC = "panic_buy";

    @Resource
    private RecordMapper recordMapper;

    @Resource
    private KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public void send(Object message) {
        String content = JSON.toJSONString(message);
        log.info("+++++++++++++++++++++  message = {}", content);
        kafkaTemplate.send(TOPIC, content);
    }

    @Override
    public Object get() {
        return null;
    }


    @KafkaListener(topics = {TOPIC})
    public void listen(ConsumerRecord<?, ?> record) {
        Optional<?> kafkaMessage = Optional.ofNullable(record.value());
        if (kafkaMessage.isPresent()) {
            Object message = kafkaMessage.get();
            log.info("获取消息record :" + record);
            RecordDO recordDO = JSON.parseObject((String) message, new TypeReference<RecordDO>() {
            });
            try {
                recordMapper.addRecord(recordDO);
            } catch (DAOException d) {
                log.info("新增交易记录失败");
            }
            log.info("新增交易记录 :" + recordDO);
        }
    }
}
