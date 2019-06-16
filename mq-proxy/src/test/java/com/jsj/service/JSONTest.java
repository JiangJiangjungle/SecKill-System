package com.jsj.service;

import com.jsj.api.entity.BuyInformation;
import com.jsj.api.util.JSONSerializer;

public class JSONTest {
    public static void main(String[] args) {
        BuyInformation information = new BuyInformation(1L,1L,1);
        byte[] buf = new JSONSerializer().serialize(information);
        System.out.println(new String(buf));
    }
}
