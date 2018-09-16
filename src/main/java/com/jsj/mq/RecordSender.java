package com.jsj.mq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RecordSender{

//    @Autowired
//    private RabbitTemplate rabbitTemplate;
//
//    @Autowired
//    private RabbitmqConfig rabbitmqConfig;
//
//    @Scheduled(fixedRate = 2000)
//    public void sendMsg(String message) {
//        CorrelationData correlationId = new CorrelationData(UUID.randomUUID().toString());
//
//        rabbitTemplate.setConfirmCallback(this);//rabbitTemplate如果为单例的话，那回调就是最后设置的内容
//        rabbitTemplate.convertAndSend(rabbitmqConfig.getExchangeName(), rabbitmqConfig.getRoutingKey()
//                , message, correlationId);
//    }
//
//    //回调
//    @Override
//    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
//        log.info(" 回调id:" + correlationData);
//        if (ack) {
//            log.info("消息成功消费");
//        } else {
//            log.info("消息消费失败:" + cause);
//        }
//    }
}
