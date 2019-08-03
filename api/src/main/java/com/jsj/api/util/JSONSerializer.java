package com.jsj.api.util;

import com.alibaba.fastjson.JSON;
import org.springframework.stereotype.Component;

/**
 * @author jiangshenjie
 * @date 2019-6-16
 */
public class JSONSerializer implements Serializer {

    @Override
    public byte[] serialize(Object t) {
        return JSON.toJSONBytes(t);
    }

    @Override
    public <T> T deserialize(String msg,Class<T> clazz){
        return JSON.parseObject(msg, clazz);
    }
}
