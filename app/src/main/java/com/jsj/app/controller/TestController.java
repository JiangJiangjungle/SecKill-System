package com.jsj.app.controller;

import com.jsj.app.constant.BuyResultEnum;
import com.jsj.app.pojo.vo.BuyInformation;
import com.jsj.app.pojo.vo.Message;
import com.jsj.app.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private TestService testService;

    @PostMapping("/0")
    public Message<?> test(@RequestBody BuyInformation buyInformation) throws Exception {
        Message<Object> message = new Message<>();
        // 获取userId
        String userId = buyInformation.getUserId();
        // 获取商品id
        String productId = buyInformation.getProductId();
        message.setStatusCode(BuyResultEnum.SUCCESS.getValue());
        message.setStatusMessage(BuyResultEnum.SUCCESS.getLabel());
        // 返回结果
        return message;
    }

    @PostMapping("/1")
    public Message<?> test1(@RequestBody BuyInformation buyInformation) throws Exception {
        Message<Object> message = new Message<>();
        BuyResultEnum result;
        // 获取userId
        String userId = buyInformation.getUserId();
        // 获取商品id
        String productId = buyInformation.getProductId();
        result = testService.handleWithoutRedis(userId, productId, 1, true);
        message.setStatusCode(result.getValue());
        message.setStatusMessage(result.getLabel());
        // 返回结果
        return message;
    }

    @PostMapping("/2")
    public Message<?> test2(@RequestBody BuyInformation buyInformation) throws Exception {
        Message<Object> message = new Message<>();
        BuyResultEnum result;
        // 获取userId
        String userId = buyInformation.getUserId();
        // 获取商品id
        String productId = buyInformation.getProductId();
        result = testService.handleWithoutRedis(userId, productId, 1, false);
        message.setStatusCode(result.getValue());
        message.setStatusMessage(result.getLabel());
        // 返回结果
        return message;
    }

    @PostMapping("/3")
    public Message<?> test3(@RequestBody BuyInformation buyInformation) throws Exception {
        Message<Object> message = new Message<>();
        BuyResultEnum result;
        // 获取userId
        String userId = buyInformation.getUserId();
        // 获取商品id
        String productId = buyInformation.getProductId();
        result = testService.handleByRedis(userId, productId, 1, true);
        message.setStatusCode(result.getValue());
        message.setStatusMessage(result.getLabel());
        // 返回结果
        return message;
    }

    @PostMapping("/4")
    public Message<?> test4(@RequestBody BuyInformation buyInformation) throws Exception {
        Message<Object> message = new Message<>();
        BuyResultEnum result;
        // 获取userId
        String userId = buyInformation.getUserId();
        // 获取商品id
        String productId = buyInformation.getProductId();
        result = testService.handleByRedis(userId, productId, 1, false);
        message.setStatusCode(result.getValue());
        message.setStatusMessage(result.getLabel());
        // 返回结果
        return message;
    }

    @PostMapping("/5")
    public Message<?> test5(@RequestBody BuyInformation buyInformation) throws Exception {
        Message<Object> message = new Message<>();
        BuyResultEnum result;
        // 获取userId
        String userId = buyInformation.getUserId();
        // 获取商品id
        String productId = buyInformation.getProductId();
        result = testService.handleByRedisAndKafka(userId, productId, 1, true);
        message.setStatusCode(result.getValue());
        message.setStatusMessage(result.getLabel());
        // 返回结果
        return message;
    }

    @PostMapping("/6")
    public Message<?> test6(@RequestBody BuyInformation buyInformation) throws Exception {
        Message<Object> message = new Message<>();
        BuyResultEnum result;
        // 获取userId
        String userId = buyInformation.getUserId();
        // 获取商品id
        String productId = buyInformation.getProductId();
        result = testService.handleByRedisAndKafka(userId, productId, 1, false);
        message.setStatusCode(result.getValue());
        message.setStatusMessage(result.getLabel());
        // 返回结果
        return message;
    }

}
