package com.jsj.mq.web;


import com.jsj.api.entity.BuyInformation;
import com.jsj.api.entity.SecKillRequest;
import com.jsj.api.util.JSONSerializer;
import com.jsj.api.util.Serializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author jiangshenjie
 * @date 2019-06-16
 */
@RestController
@RequestMapping("/sec-kill")
public class ProxyController {
    public static Serializer serializer = new JSONSerializer();
    @Value("${spring.kafka.consumer.request-topic}")
    private String requestTopic;
    @Autowired
    KafkaTemplate kafkaTemplate;

    @PostMapping("")
    public String secKill(@RequestBody BuyInformation buyInformation) throws Exception {
        SecKillRequest request = new SecKillRequest(System.currentTimeMillis(), buyInformation);
        byte[] buf = serializer.serialize(request);
        //同步模式发送消息
        kafkaTemplate.send(requestTopic, new String(buf)).get();
        return "秒杀请求已发送";
    }
}
