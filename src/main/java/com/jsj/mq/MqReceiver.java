package com.jsj.mq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RabbitListener(queues = "recordStorageStoQueue")
public class MqReceiver {

    @Autowired
    private PanicBuyingMapper panicBuyingMapper;


//    @RabbitHandler
//    public void process(String message) throws Exception {
//
////        RecordPO record = JSON.parseObject(message, new TypeReference<RecordPO>() {
////        });
////        int productId = record.getProductId();
////        //插入record
////        if (panicBuyingMapper.addRecord(record) && panicBuyingMapper.decreaseProductStock(productId)) {
////            log.info("插入交易记录成功");
////        } else {
////            log.info("插入交易记录失败");
////        }
//    }
}
