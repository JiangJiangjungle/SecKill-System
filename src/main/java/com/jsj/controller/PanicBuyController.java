package com.jsj.controller;

import com.jsj.exception.ServiceException;
import com.jsj.service.PanicBuyService;
import com.jsj.pojo.ServiceResult;
import com.jsj.pojo.vo.common.Message;
import com.jsj.pojo.vo.req.BuyInformation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Slf4j
@RequestMapping("/buy")
@RestController
public class PanicBuyController {

    @Resource
    private PanicBuyService panicBuyService;

    /**
     * MySQL乐观锁实现抢购
     *
     * @param buyInformation
     * @return
     */
    @PostMapping("/ByOptimisticLock")
    public Message<?> handleByOptimisticLock(@RequestBody BuyInformation buyInformation) {
        log.info("调用接口/buy/ByOptimisticLock");
        Message<Object> message = new Message<>();
        ServiceResult result;
        //参数判空
        if (buyInformation == null) {
            result = ServiceResult.PARAMS_ERROR;
            message.setStatusCode(result.getValue());
            message.setStatusMessage(result.getLabel());
            return message;
        }
        //获取userId
        String userId = buyInformation.getUserId();
        //获取商品id
        String productId = buyInformation.getProductId();
        //参数判空
        if (StringUtils.isEmpty(userId) || StringUtils.isEmpty(productId)) {
            result = ServiceResult.PARAMS_ERROR;
            message.setStatusCode(result.getValue());
            message.setStatusMessage(result.getLabel());
            return message;
        }
        //利用乐观锁参与抢购，返回抢购结果
        try {
            result = panicBuyService.handleByOptimisticLock(userId, productId, 1);
            message.setStatusCode(result.getValue());
            message.setStatusMessage(result.getLabel());
        } catch (ServiceException s) {
            result = ServiceResult.SYSTEM_EXCEPTION;
            message.setStatusCode(result.getValue());
            message.setStatusMessage(result.getLabel());
            return message;
        }
        //返回结果
        return message;
    }
}
