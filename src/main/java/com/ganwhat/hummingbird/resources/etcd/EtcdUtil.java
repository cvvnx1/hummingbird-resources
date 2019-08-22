package com.ganwhat.hummingbird.resources.etcd;

import lombok.Setter;
import mousio.client.retry.RetryOnce;
import mousio.etcd4j.EtcdClient;
import mousio.etcd4j.responses.EtcdAuthenticationException;
import mousio.etcd4j.responses.EtcdException;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeoutException;

/**
 * @author : yaodongliu
 */
public class EtcdUtil {
    private EtcdClient client;

    @Setter
    private int ttl = 60;

    // 这里就是发布的节点
    private String etcdKey = "hummingbird/";

    public EtcdUtil() {
        // 节点数
        int nodeCount = 3;

        // 添加集群节点
        URI[] uris = new URI[nodeCount];
        for (int i = 0; i < nodeCount; i++) {
            String urlString = "etcdHost" + i;
            uris[i] = URI.create(urlString);
        }
        client = new EtcdClient(uris);
        // retry策略
        client.setRetryHandler(new RetryOnce(20));
    }

    public EtcdUtil(Class<?> clazz, String... peerUrls) {
        etcdKey = etcdKey + clazz.getName();
        List<String> peerUrlList = Arrays.asList(peerUrls);
        client = new EtcdClient(peerUrlList.stream().map(URI::create).toArray(URI[]::new));
        // retry策略
        client.setRetryHandler(new RetryOnce(20));
    }

    public void lock() throws Exception {
        client.put(etcdKey, "lock").ttl(ttl).send().get();
        // 加上这个get()用来保证设置完成，走下一步，get会阻塞，由上面client的retry策略决定阻塞的方式

        // 启动一个守护线程来定时刷新节点
        new Thread(new GuardEtcd()).start();
    }

    public boolean haveLocked() {
        try {
            return "lock".equals(client.get(etcdKey).send().get().getNode().getValue());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void unlock() {
        try {
            client.delete(etcdKey).send().get();
            client.close();
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

                    client.refresh(etcdKey, ttl).send();
                    System.out.println("心跳");
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
