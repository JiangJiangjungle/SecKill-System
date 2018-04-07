package com.jsj.mq;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.jsj.bean.Record;
import com.jsj.mapper.PanicBuyingMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
@RabbitListener(queues = "recordStorageStoQueue")
public class MqReceiver {

    @Autowired
    PanicBuyingMapper panicBuyingMapper;

    AtomicInteger integer = new AtomicInteger(0);

    @RabbitHandler
    public void process(String message) throws Exception {
        log.info("模拟-插入交易记录： "+message+" 成功！count: "+integer.incrementAndGet());
//        Record record = JSON.parseObject(message, new TypeReference<Record>() {
//        });
//        int productId = record.getProductId();
//        //插入record
//        if (panicBuyingMapper.addRecord(record) && panicBuyingMapper.decreaseProductStock(productId)) {
//            log.info("插入交易记录成功");
//        } else {
//            log.info("插入交易记录失败");
//        }
    }
}
