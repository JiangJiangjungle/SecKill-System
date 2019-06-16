package com.jsj.api.util;

/**
 * @param <T>
 * @author jiangshenjie
 * @date 2019-6-16
 */
public interface CacheUtil<T> {
    T getResource();

    void releaseResource(T t);
}
