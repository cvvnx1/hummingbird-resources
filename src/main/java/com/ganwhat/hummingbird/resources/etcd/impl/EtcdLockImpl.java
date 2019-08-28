package com.ganwhat.hummingbird.resources.etcd.impl;

import com.ganwhat.hummingbird.resources.etcd.EtcdLock;
import com.ganwhat.hummingbird.resources.etcd.EtcdUtil;
import com.ganwhat.hummingbird.resources.etcd.exception.EtcdLockException;
import mousio.etcd4j.responses.EtcdAuthenticationException;
import mousio.etcd4j.responses.EtcdException;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author : yaodongliu
 * @date : 2019/8/25
 */
public class EtcdLockImpl implements EtcdLock {

    private EtcdUtil etcdUtil;

    private int ttl = 20;

    // 这里就是发布的节点
    private String etcdKey;

    public EtcdLockImpl(Class<?> clazz, String... peerUrls) {
        etcdKey = "hummingbird/" + clazz.getName();
        etcdUtil = new EtcdUtil(peerUrls);
        etcdUtil.setTtl(ttl);
    }

    @Override
    public void tryLock() throws Exception {
        if (!haveLocked()) {
            lock();
        } else {
            throw new EtcdLockException();
        }
    }

    @Override
    public void lock() throws Exception {
        etcdUtil.putNode(etcdKey, "lock");
        // 启动一个守护线程来定时刷新节点
        new Thread(new EtcdLockImpl.GuardEtcd()).start();
    }

    @Override
    public boolean haveLocked() {
        try {
            return "lock".equals(etcdUtil.getNodeValue(etcdKey));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void unlock() {
        try {
            etcdUtil.removeNode(etcdKey);
        } catch (IOException | EtcdException | EtcdAuthenticationException | TimeoutException e) {
            e.printStackTrace();
        }
    }

    private class GuardEtcd implements Runnable {
        @Override
        public void run() {
            while (true) {
                try {
                    // ttl的0.8倍
                    Thread.sleep(ttl * 800L);

                    etcdUtil.refreshNode(etcdKey);
                    System.out.println("心跳");
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
