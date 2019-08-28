package com.ganwhat.hummingbird.resources.etcd.exception;

/**
 * @author : yaodongliu
 * @date : 2019/8/28
 */
public class EtcdLockException extends Exception {
    public EtcdLockException() {
        super();
    }

    public EtcdLockException(String msg) {
        super(msg);
    }
}
