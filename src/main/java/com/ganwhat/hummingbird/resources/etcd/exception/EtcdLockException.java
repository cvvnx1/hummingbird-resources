package com.ganwhat.hummingbird.resources.etcd.exception;

/**
 * @author : yaodongliu
 */
public class EtcdLockException extends Exception {
    public EtcdLockException() {
        super();
    }

    public EtcdLockException(String msg) {
        super(msg);
    }
}
