package com.jsj.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;

/**
 * kafka消息队列工具类
 */
public interface KafkaUtils {
    /**
     * 发送消息到队列
     *
     * @param message
     */
    void send(Object message);

    /**
     * 接收消息
     *
     * @return
     */
    Object get();

}
