package com.ganwhat.hummingbird.resources.etcd;

import com.ganwhat.hummingbird.resources.etcd.impl.EtcdLockImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @Auther: yaodongliu
 * @Date: 2019/8/19 23:00
 */
public class EtcdUtilTest {

    private static EtcdLock etcdLock;

    @BeforeEach
    public void setEtcd() {
        etcdLock = new EtcdLockImpl(EtcdUtilTest.class, "http://localhost:4001");
    }

    @Test
    public void testLock() throws Exception {
        etcdLock.setEtcdKey(etcdLock.getEtcdKey() + "/" + Thread.currentThread() .getStackTrace()[1].getMethodName());
        etcdLock.lock();
        Assertions.assertTrue(etcdLock.haveLocked());
        etcdLock.unlock();
    }
}
