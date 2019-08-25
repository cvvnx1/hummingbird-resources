package com.ganwhat.hummingbird.resources.etcd;

/**
 * @author : yaodongliu
 * @date : 2019/8/25
 */
public interface EtcdLock {

    void lock() throws Exception;

    boolean haveLocked();

    void unlock();

}
