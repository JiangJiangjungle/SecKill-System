package com.jsj.app.controller;

import com.jsj.app.constant.BuyResultEnum;
import com.jsj.app.exception.ServiceException;
import com.jsj.app.pojo.vo.BuyInformation;
import com.jsj.app.pojo.vo.Message;
import com.jsj.app.service.PanicBuyService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author com.jsj
 * @date 2018-9-22
 */
@Slf4j
@RequestMapping("/buy")
@RestController
public class PanicBuyController {

    @Resource
    private PanicBuyService panicBuyService;

    /**
     * MySQL乐观锁实现抢购
     *
     * @param buyInformation 抢购请求信息
     * @return
     */
    @PostMapping("/ByOptimisticLock")
    public Message<?> handleByOptimisticLock(@RequestBody BuyInformation buyInformation) {
        log.info("调用接口/buy/ByOptimisticLock");
        Message<Object> message = new Message<>();
        BuyResultEnum result;
        // 参数判空
        if (buyInformation == null) {
            result = BuyResultEnum.PARAMS_ERROR;
            message.setStatusCode(result.getValue());
            message.setStatusMessage(result.getLabel());
            return message;
        }
        // 获取userId
        String userId = buyInformation.getUserId();
        // 获取商品id
        String productId = buyInformation.getProductId();
        // 参数判空
        if (StringUtils.isEmpty(userId) || StringUtils.isEmpty(productId)) {
            result = BuyResultEnum.PARAMS_ERROR;
            message.setStatusCode(result.getValue());
            message.setStatusMessage(result.getLabel());
            return message;
        }
        // 利用乐观锁参与抢购，返回抢购结果
        try {
            result = panicBuyService.handleByOptimisticLock(userId, productId, 1);
            message.setStatusCode(result.getValue());
            message.setStatusMessage(result.getLabel());
        } catch (ServiceException s) {
            result = BuyResultEnum.SYSTEM_EXCEPTION;
            message.setStatusCode(result.getValue());
            message.setStatusMessage(result.getLabel());
            return message;
        }
        // 返回结果
        return message;
    }

    /**
     * 利用Mysql悲观锁实现抢购
     *
     * @param buyInformation 抢购请求信息
     * @return
     */
    @PostMapping("/ByPessimisticLock")
    public Message<?> handleByPessimisticLock(@RequestBody BuyInformation buyInformation) {
        log.info("调用接口：/buy/ByRedisLock");
        Message<Object> message = new Message<>();
        BuyResultEnum result;
        // 参数判空
        if (buyInformation == null) {
            result = BuyResultEnum.PARAMS_ERROR;
            message.setStatusCode(result.getValue());
            message.setStatusMessage(result.getLabel());
            return message;
        }
        // 获取userId
        String userId = buyInformation.getUserId();
        // 获取商品id
        String productId = buyInformation.getProductId();
        // 参数判空
        if (StringUtils.isEmpty(userId) || StringUtils.isEmpty(productId)) {
            result = BuyResultEnum.PARAMS_ERROR;
            message.setStatusCode(result.getValue());
            message.setStatusMessage(result.getLabel());
            return message;
        }
        // 利用MySQL悲观锁参与抢购，返回抢购结果
        try {
            result = panicBuyService.handleByPessimisticLock(userId, productId, 1);
            message.setStatusCode(result.getValue());
            message.setStatusMessage(result.getLabel());
        } catch (ServiceException s) {
            result = BuyResultEnum.SYSTEM_EXCEPTION;
            message.setStatusCode(result.getValue());
            message.setStatusMessage(result.getLabel());
            return message;
        }
        // 返回结果
        return message;
    }

    /**
     * 利用redis分布式锁实现抢购
     *
     * @param buyInformation 抢购请求信息
     * @return
     */
    @PostMapping("/ByRedisLock")
    public Message<?> handleByRedisLock(@RequestBody BuyInformation buyInformation) {
        log.info("调用接口：/buy/ByRedisLock");
        Message<Object> message = new Message<>();
        BuyResultEnum result;
        // 参数判空
        if (buyInformation == null) {
            result = BuyResultEnum.PARAMS_ERROR;
            message.setStatusCode(result.getValue());
            message.setStatusMessage(result.getLabel());
            return message;
        }
        // 获取userId
        String userId = buyInformation.getUserId();
        // 获取商品id
        String productId = buyInformation.getProductId();
        // 参数判空
        if (StringUtils.isEmpty(userId) || StringUtils.isEmpty(productId)) {
            result = BuyResultEnum.PARAMS_ERROR;
            message.setStatusCode(result.getValue());
            message.setStatusMessage(result.getLabel());
            return message;
        }
        // 利用redis分布锁参与抢购，返回抢购结果
        try {
            result = panicBuyService.handleByRedisLock(userId, productId, 1);
            message.setStatusCode(result.getValue());
            message.setStatusMessage(result.getLabel());
        } catch (ServiceException s) {
            result = BuyResultEnum.SYSTEM_EXCEPTION;
            message.setStatusCode(result.getValue());
            message.setStatusMessage(result.getLabel());
            return message;
        }
        // 返回结果
        return message;
    }

    /**
     * 利用zookeeper分布式锁实现抢购
     *
     * @param buyInformation 抢购请求信息
     * @return
     */
    @PostMapping("/ByZookeeperLock")
    public Message<?> handleByZookeeperLock(@RequestBody BuyInformation buyInformation) {
        log.info("调用接口：/buy/ByZookeeperLock");
        Message<Object> message = new Message<>();
        BuyResultEnum result;
        // 参数判空
        if (buyInformation == null) {
            result = BuyResultEnum.PARAMS_ERROR;
            message.setStatusCode(result.getValue());
            message.setStatusMessage(result.getLabel());
            return message;
        }
        // 获取userId
        String userId = buyInformation.getUserId();
        // 获取商品id
        String productId = buyInformation.getProductId();
        // 参数判空
        if (StringUtils.isEmpty(userId) || StringUtils.isEmpty(productId)) {
            result = BuyResultEnum.PARAMS_ERROR;
            message.setStatusCode(result.getValue());
            message.setStatusMessage(result.getLabel());
            return message;
        }
        // 利用zookeeper分布锁参与抢购，返回抢购结果
        try {
            result = panicBuyService.handleByZookeeperLock(userId, productId, 1);
            message.setStatusCode(result.getValue());
            message.setStatusMessage(result.getLabel());
        } catch (ServiceException s) {
            result = BuyResultEnum.SYSTEM_EXCEPTION;
            message.setStatusCode(result.getValue());
            message.setStatusMessage(result.getLabel());
            return message;
        }
        // 返回结果
        return message;
    }
}
