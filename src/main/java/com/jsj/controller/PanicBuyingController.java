package com.jsj.controller;

import com.jsj.constant.ServiceResult;
import com.jsj.service.PanicBuyingService;
import com.jsj.web.common.Message;
import com.jsj.web.req.PanicBuyingRequest;
import com.jsj.web.vo.PanicBuyingResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RequestMapping("/product")
@RestController
public class PanicBuyingController {

    /**
     * MySQL行锁
     *
     * @param requestMessage
     * @return
     */
    @RequestMapping(value = "/pessLockInMySQL", method = RequestMethod.POST)
    public Message<PanicBuyingResponse> pessLockInMySQL(@RequestBody Message<PanicBuyingRequest> requestMessage) {
        return null;
    }

    /**
     * 利用redis的watch监控的特性
     *
     * @throws InterruptedException
     */
    @RequestMapping(value = "/baseOnRedisWatch", method = RequestMethod.POST)
    public Message<PanicBuyingResponse> baseOnRedisWatch(@RequestBody Message<PanicBuyingRequest> requestMessage) throws InterruptedException {
        return null;
    }

    /**
     * 利用AtomicInteger的CAS机制特性
     *
     * @param requestMessage
     * @return
     */
    @RequestMapping(value = "/baseOnAtomicInteger", method = RequestMethod.POST)
    public Message<PanicBuyingResponse> baseOnAtomicInteger(@RequestBody Message<PanicBuyingRequest> requestMessage) {
        return null;
    }
}
