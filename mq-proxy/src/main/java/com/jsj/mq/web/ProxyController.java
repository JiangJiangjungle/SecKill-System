package com.jsj.mq.web;


import com.jsj.api.BuyResultEnum;
import com.jsj.api.entity.BuyInformation;
import com.jsj.api.entity.SecKillRequest;
import com.jsj.api.util.CacheUtil;
import com.jsj.api.util.JSONSerializer;
import com.jsj.api.util.Serializer;
import com.jsj.mq.util.JedisUtilImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.WebAsyncTask;
import redis.clients.jedis.Jedis;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

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
    @Value("${data.redis.product_user_list_prefix}")
    private String productUserListPrefix;
    @Autowired
    KafkaTemplate kafkaTemplate;
    @Autowired
    CacheUtil<Jedis> jedisCacheUtil;

    @PostMapping("")
    public WebAsyncTask<String> secKill(@RequestBody BuyInformation buyInformation) throws Exception {
        WebAsyncTask<String> wat;
        try (Jedis jedis = jedisCacheUtil.getResource()) {
            if (jedis.sismember(productUserListPrefix + buyInformation.getProductId(), String.valueOf(buyInformation.getUserId()))) {
                return new WebAsyncTask<>(10000L, () -> "重复秒杀！");
            }
        }
        //同步模式发送消息
        SecKillRequest request = new SecKillRequest(System.currentTimeMillis(), buyInformation);
        byte[] buf = serializer.serialize(request);
        kafkaTemplate.send(requestTopic, new String(buf)).get();
        //异步线程等待秒杀结果
        Callable<String> result = () -> {
            try (Jedis jedis = jedisCacheUtil.getResource()) {
                do {
                    TimeUnit.SECONDS.sleep(2);
                } while (!jedis.sismember(productUserListPrefix + buyInformation.getProductId(), String.valueOf(buyInformation.getUserId())));
            }
            return "秒杀成功！";
        };
        wat = new WebAsyncTask<>(10000L, result);
        wat.onTimeout(() -> "秒杀失败，请重试。。");
        return wat;
    }
}
