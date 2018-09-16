package com.jsj.controller;

import com.jsj.exception.ServiceException;
import com.jsj.service.PanicBuyService;
import com.jsj.util.ServiceResult;
import com.jsj.vo.common.Message;
import com.jsj.vo.req.BuyInformation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RequestMapping("/buy")
@RestController
public class PanicBuyController {

    @Resource
    private PanicBuyService panicBuyService;

    /**
     * MySQL悲观锁实现抢购
     * @param buyInformation
     * @return
     */
    @PostMapping("/ByPessimisticLock")
    public Message<?> handleByPessimisticLock(@RequestBody BuyInformation buyInformation) {
        Message<Object> message = new Message<>();
        if (buyInformation!=null){
            String userId = buyInformation.getUserId();
            String productId = buyInformation.getProductId();
            if (!StringUtils.isEmpty(userId)&&!StringUtils.isEmpty(productId)){
                try {
                    ServiceResult serviceResult = panicBuyService.handleByPessimisticLock(userId, productId, 1);
                    message.setStatusCode(serviceResult.getValue());
                    message.setStatusMessage(serviceResult.getLabel());
                }catch (ServiceException s){
                    message.setStatusCode(ServiceResult.SYSTEM_EXCEPTION.getValue());
                    message.setStatusMessage(ServiceResult.SYSTEM_EXCEPTION.getLabel());

                }
                return message;
            }
        }
        message.setStatusCode(ServiceResult.PARAMS_ERROR.getValue());
        message.setStatusMessage(ServiceResult.PARAMS_ERROR.getLabel());
        return message;
    }
}
