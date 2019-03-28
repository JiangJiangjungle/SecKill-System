package com.jsj.consumer.util;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Optional;

@Slf4j
@Component
public class KafkaUtilsImpl implements KafkaUtils {

    private final String TOPIC = "panic_buy";
//
//    @Autowired
//    private RecordMapper recordMapper;

    @Resource
    private KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public void send(Object message) {
        String content = JSON.toJSONString(message);
        kafkaTemplate.send(TOPIC, content);
        log.info("+++++++++++++++++++++  message = {}", content);
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
//            RecordDO recordDO = JSON.parseObject((String) message, new TypeReference<RecordDO>() {
//            });
//            try {
//                recordMapper.addRecord(recordDO);
//            } catch (DAOException d) {
//                log.info("新增交易记录失败");
//            }
//            log.info("新增交易记录 :" + recordDO);
        }
    }
}
