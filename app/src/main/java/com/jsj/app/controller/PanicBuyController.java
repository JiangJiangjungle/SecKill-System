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
import java.time.LocalTime;

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
    public Message<?> handleByOptimisticLock(@RequestBody BuyInformation buyInformation) throws Exception{
        Message<Object> message = new Message<>();
        BuyResultEnum result;
        // 参数判空
        if (!legalParam(buyInformation)) {
            result = BuyResultEnum.PARAMS_ERROR;
            message.setStatusCode(result.getValue());
            message.setStatusMessage(result.getLabel());
            return message;
        }
        // 获取userId
        String userId = buyInformation.getUserId();
        // 获取商品id
        String productId = buyInformation.getProductId();
        // 利用乐观锁参与抢购，返回抢购结果
        result = panicBuyService.handleByOptimisticLock(userId, productId, 1);
        message.setStatusCode(result.getValue());
        message.setStatusMessage(result.getLabel());
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
    public Message<?> handleByRedisLock(@RequestBody BuyInformation buyInformation) throws Exception {
        Message<Object> message = new Message<>();
        BuyResultEnum result;
        // 参数判空
        if (!legalParam(buyInformation)) {
            result = BuyResultEnum.PARAMS_ERROR;
            message.setStatusCode(result.getValue());
            message.setStatusMessage(result.getLabel());
            return message;
        }
        // 获取userId
        String userId = buyInformation.getUserId();
        // 获取商品id
        String productId = buyInformation.getProductId();
        // 利用redis分布锁参与抢购，返回抢购结果
        result = panicBuyService.handleByRedisLock(userId, productId, 1);
        message.setStatusCode(result.getValue());
        message.setStatusMessage(result.getLabel());
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
    public Message<?> handleByZookeeperLock(@RequestBody BuyInformation buyInformation)  throws Exception{
        Message<Object> message = new Message<>();
        BuyResultEnum result;
        // 参数判空
        if (!legalParam(buyInformation)) {
            result = BuyResultEnum.PARAMS_ERROR;
            message.setStatusCode(result.getValue());
            message.setStatusMessage(result.getLabel());
            return message;
        }
        // 获取userId
        String userId = buyInformation.getUserId();
        // 获取商品id
        String productId = buyInformation.getProductId();
        // 利用zookeeper分布锁参与抢购，返回抢购结果
        result = panicBuyService.handleByZookeeperLock(userId, productId, 1);
        message.setStatusCode(result.getValue());
        message.setStatusMessage(result.getLabel());
        // 返回结果
        return message;
    }

    /**
     * 参数检查
     *
     * @param buyInformation 请求参数
     * @return
     */
    private boolean legalParam(BuyInformation buyInformation) {
        // 参数判空
        return buyInformation != null &&
                !StringUtils.isEmpty(buyInformation.getUserId()) &&
                !StringUtils.isEmpty(buyInformation.getProductId());
    }
}
