package com.jsj;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.jsj.web.common.Message;
import com.jsj.web.req.PanicBuyingRequest;

public class JSONTest {

    public static void main(String[] args) {
        PanicBuyingRequest request = new PanicBuyingRequest();
        request.setUserId(1);
        request.setProductId(4);
        Message<PanicBuyingRequest> requestMessage = new Message<PanicBuyingRequest>(request);
        String msg = JSON.toJSONString(requestMessage);
        System.out.println(msg);

        Message<PanicBuyingRequest> requestMessage1 = JSON.parseObject(msg, new TypeReference<Message<PanicBuyingRequest>>() {
        });

        System.out.println(requestMessage1.toString());
    }
}
