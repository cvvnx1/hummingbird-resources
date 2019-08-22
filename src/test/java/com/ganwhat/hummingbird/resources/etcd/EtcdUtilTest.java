package com.ganwhat.hummingbird.resources.etcd;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @Auther: yaodongliu
 * @Date: 2019/8/19 23:00
 */
public class EtcdUtilTest {

    private static EtcdUtil etcdUtil;

    @BeforeEach
    public void setEtcd() {
        etcdUtil = new EtcdUtil(EtcdUtilTest.class, "http://localhost:4001");
    }

    @Test
    public void testLock() throws Exception {
        etcdUtil.lock();
        Assertions.assertTrue(etcdUtil.haveLocked());
        etcdUtil.unlock();
    }
}
