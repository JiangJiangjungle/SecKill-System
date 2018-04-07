package com.jsj.mq;

import com.jsj.config.RabbitmqConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
public class RecordSender implements RabbitTemplate.ConfirmCallback {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RabbitmqConfig rabbitmqConfig;

    public void sendMsg(String message) {
        CorrelationData correlationId = new CorrelationData(UUID.randomUUID().toString());

        this.rabbitTemplate.setConfirmCallback(this);//rabbitTemplate如果为单例的话，那回调就是最后设置的内容
        this.rabbitTemplate.convertAndSend(rabbitmqConfig.getExchangeName(), rabbitmqConfig.getRoutingKey()
                , message, correlationId);
    }

    //回调
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        log.info(" 回调id:" + correlationData);
        if (ack) {
            log.info("消息成功消费");
        } else {
            log.info("消息消费失败:" + cause);
        }
    }
}
