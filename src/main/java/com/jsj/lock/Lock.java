package com.jsj.lock;

public interface Lock {
    void lock();

    boolean tryLock();

    void unlock();
}
