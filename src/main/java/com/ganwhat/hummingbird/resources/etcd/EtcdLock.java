package com.ganwhat.hummingbird.resources.etcd;

/**
 * @author : yaodongliu
 * @date : 2019/8/25
 */
public interface EtcdLock {

    String getEtcdKey();

    void setEtcdKey(String etcdKey);

    void tryLock() throws Exception;

    void lock() throws Exception;

    boolean haveLocked();

    void unlock();

}
