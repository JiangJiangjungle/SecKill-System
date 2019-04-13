package com.jsj.app.web;

import com.jsj.app.common.BuyResultEnum;
import com.jsj.app.pojo.vo.BuyInformation;
import com.jsj.app.pojo.vo.Message;
import com.jsj.app.service.SecKillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private SecKillService secKillService;

    /**
     * 数据库的乐观锁模式
     * @param buyInformation
     * @return
     * @throws Exception
     */
    @PostMapping("/1")
    public Message<?> test1(@RequestBody BuyInformation buyInformation) throws Exception {
        Message<Object> message = new Message<>();
        BuyResultEnum result;
        // 获取userId
        String userId = buyInformation.getUserId();
        // 获取商品id
        String productId = buyInformation.getProductId();
        result = secKillService.handleWithoutRedis(userId, productId, 1, true);
        message.setStatusCode(result.getValue());
        message.setStatusMessage(result.getLabel());
        // 返回结果
        return message;
    }

    /**
     * 数据库的悲观锁模式
     * @param buyInformation
     * @return
     * @throws Exception
     */
    @PostMapping("/2")
    public Message<?> test2(@RequestBody BuyInformation buyInformation) throws Exception {
        Message<Object> message = new Message<>();
        BuyResultEnum result;
        // 获取userId
        String userId = buyInformation.getUserId();
        // 获取商品id
        String productId = buyInformation.getProductId();
        result = secKillService.handleWithoutRedis(userId, productId, 1, false);
        message.setStatusCode(result.getValue());
        message.setStatusMessage(result.getLabel());
        // 返回结果
        return message;
    }

    /**
     * redis+数据库的乐观锁模式
     * @param buyInformation
     * @return
     * @throws Exception
     */
    @PostMapping("/3")
    public Message<?> test3(@RequestBody BuyInformation buyInformation) throws Exception {
        Message<Object> message = new Message<>();
        BuyResultEnum result;
        // 获取userId
        String userId = buyInformation.getUserId();
        // 获取商品id
        String productId = buyInformation.getProductId();
        result = secKillService.handleByRedis(userId, productId, 1, true);
        message.setStatusCode(result.getValue());
        message.setStatusMessage(result.getLabel());
        // 返回结果
        return message;
    }

    /**
     * redis+数据库的悲观锁模式
     * @param buyInformation
     * @return
     * @throws Exception
     */
    @PostMapping("/4")
    public Message<?> test4(@RequestBody BuyInformation buyInformation) throws Exception {
        Message<Object> message = new Message<>();
        BuyResultEnum result;
        // 获取userId
        String userId = buyInformation.getUserId();
        // 获取商品id
        String productId = buyInformation.getProductId();
        result = secKillService.handleByRedis(userId, productId, 1, false);
        message.setStatusCode(result.getValue());
        message.setStatusMessage(result.getLabel());
        // 返回结果
        return message;
    }

    /**
     * redis+数据库的乐观锁模式+Kafka
     * @param buyInformation
     * @return
     * @throws Exception
     */
    @PostMapping("/5")
    public Message<?> test5(@RequestBody BuyInformation buyInformation) throws Exception {
        Message<Object> message = new Message<>();
        BuyResultEnum result;
        // 获取userId
        String userId = buyInformation.getUserId();
        // 获取商品id
        String productId = buyInformation.getProductId();
        result = secKillService.handleByRedisAndKafka(userId, productId, 1, true);
        message.setStatusCode(result.getValue());
        message.setStatusMessage(result.getLabel());
        // 返回结果
        return message;
    }

    /**
     * redis+数据库的悲观锁模式+Kafka
     * @param buyInformation
     * @return
     * @throws Exception
     */
    @PostMapping("/6")
    public Message<?> test6(@RequestBody BuyInformation buyInformation) throws Exception {
        Message<Object> message = new Message<>();
        BuyResultEnum result;
        // 获取userId
        String userId = buyInformation.getUserId();
        // 获取商品id
        String productId = buyInformation.getProductId();
        result = secKillService.handleByRedisAndKafka(userId, productId, 1, false);
        message.setStatusCode(result.getValue());
        message.setStatusMessage(result.getLabel());
        // 返回结果
        return message;
    }

    /**
     * redis+redis分布式锁+数据库的悲观锁模式+Kafka
     * @param buyInformation
     * @return
     * @throws Exception
     */
    @PostMapping("/7")
    public Message<?> test7(@RequestBody BuyInformation buyInformation) throws Exception {
        Message<Object> message = new Message<>();
        BuyResultEnum result;
        // 获取userId
        String userId = buyInformation.getUserId();
        // 获取商品id
        String productId = buyInformation.getProductId();
        result = secKillService.handleByRedisAndKafkaAndDistributedLock(userId, productId, 1, false, true);
        message.setStatusCode(result.getValue());
        message.setStatusMessage(result.getLabel());
        // 返回结果
        return message;
    }

    /**
     * redis+zookeeper分布式锁+数据库的悲观锁模式+Kafka
     * @param buyInformation
     * @return
     * @throws Exception
     */
    @PostMapping("/8")
    public Message<?> test8(@RequestBody BuyInformation buyInformation) throws Exception {
        Message<Object> message = new Message<>();
        BuyResultEnum result;
        // 获取userId
        String userId = buyInformation.getUserId();
        // 获取商品id
        String productId = buyInformation.getProductId();
        result = secKillService.handleByRedisAndKafkaAndDistributedLock(userId, productId, 1, false, false);
        message.setStatusCode(result.getValue());
        message.setStatusMessage(result.getLabel());
        // 返回结果
        return message;
    }
}
