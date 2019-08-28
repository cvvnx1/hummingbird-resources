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

    public EtcdUtil(String... peerUrls) {
        List<String> peerUrlList = Arrays.asList(peerUrls);
        client = new EtcdClient(peerUrlList.stream().map(URI::create).toArray(URI[]::new));
        // retry策略
        client.setRetryHandler(new RetryOnce(20));
    }

    public void putNode(String key, String value) throws IOException, EtcdAuthenticationException, TimeoutException, EtcdException {
        putNode(key, value, ttl);
    }

    public void putNode(String key, String value, int ttl) throws IOException, EtcdAuthenticationException, TimeoutException, EtcdException {
        client.put(key, value).ttl(ttl).send().get();
        // 加上这个get()用来保证设置完成，走下一步，get会阻塞，由上面client的retry策略决定阻塞的方式
    }

    public void refreshNode(String key) throws IOException {
        refreshNode(key, ttl);
    }

    public void refreshNode(String key, int ttl) throws IOException {
        client.refresh(key, ttl).send();
    }

    public String getNodeValue(String key) throws IOException, EtcdAuthenticationException, TimeoutException, EtcdException {
        return client.get(key).send().get().getNode().getValue();
    }

    public void removeNode(String key) throws IOException, EtcdAuthenticationException, TimeoutException, EtcdException {
        client.delete(key).send().get();
        client.close();
    }

}
