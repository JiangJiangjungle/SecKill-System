package com.jsj.lock;

/**
 * @author jsj
 * @date 2018-9-22
 */
public interface Lock {
    void lock();

    boolean tryLock();

    void unlock();
}
