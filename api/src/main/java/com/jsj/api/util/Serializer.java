package com.jsj.api.util;

/**
 * 序列化工具
 *
 * @author jiangshenjie
 * @date 2019-6-15
 */
public interface Serializer {

    /**
     * 序列化
     *
     * @param t
     */
    public byte[] serialize(Object t);


    /**
     * 反序列化
     *
     * @return T<T>
     */
    public <T> T deserialize(String msg,Class<T> clazz);
}
