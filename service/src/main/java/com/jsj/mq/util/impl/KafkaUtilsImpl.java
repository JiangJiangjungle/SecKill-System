//package com.jsj.mq.util.impl;
//
//import com.alibaba.fastjson.JSON;
//
//import com.alibaba.fastjson.TypeReference;
//import com.jsj.api.entity.RecordDO;
//import com.jsj.api.exception.DAOException;
//import com.jsj.data.mappers.RecordMapper;
//import com.jsj.mq.util.KafkaUtils;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.ibatis.type.Alias;
//import org.apache.kafka.clients.consumer.ConsumerRecord;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.kafka.support.SendResult;
//import org.springframework.stereotype.Component;
//import org.springframework.util.concurrent.ListenableFuture;
//
//import javax.annotation.Resource;
//import java.util.Optional;
//
//@Slf4j
//@Component
//@Alias("kafkaUtils")
//public class KafkaUtilsImpl implements KafkaUtils {
//
//    private final String TOPIC = "panic_buy";
//
//    @Autowired
//    private RecordMapper recordMapper;
//
//    @Resource
//    private KafkaTemplate<String, String> kafkaTemplate;
//
//    @Override
//    public void send(Object message) throws Exception {
//        String content = JSON.toJSONString(message);
//        ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(TOPIC, content);
//        SendResult<String, String> result = future.get();
//        log.info("+++++++++++++++++++++  发送消息 = {}", result.getProducerRecord());
//    }
//
//    @Override
//    public Object get() {
//        return null;
//    }
//
//
//    @KafkaListener(topics = {TOPIC})
//    public void listen(ConsumerRecord<?, ?> record) {
//        Optional<?> kafkaMessage = Optional.ofNullable(record.value());
//        if (kafkaMessage.isPresent()) {
//            Object message = kafkaMessage.get();
//            log.info("拉取消息:" + record);
//            RecordDO recordDO = JSON.parseObject((String) message, new TypeReference<RecordDO>() {
//            });
//            try {
//                recordMapper.addRecord(recordDO);
//                log.info("数据库新增交易记录 :" + recordDO.toString());
//            } catch (DAOException d) {
//                log.info("数据库插入交易记录失败");
//            }
//        }
//    }
//}
