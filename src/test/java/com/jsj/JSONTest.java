package com.jsj;


import com.alibaba.fastjson.JSON;
import com.jsj.pojo.vo.BuyInformation;

public class JSONTest {

    public static void main(String[] args) {
        BuyInformation buyInformation = new BuyInformation();
        buyInformation.setUserId("64fa02bf-7853-4d11-a671-fe4e72ddbf49");
        buyInformation.setProductId("909133bb-2930-40a7-a759-281178ade236");
        System.out.println(JSON.toJSONString(buyInformation));
    }
}
