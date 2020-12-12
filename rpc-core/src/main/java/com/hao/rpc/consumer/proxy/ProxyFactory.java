package com.hao.rpc.consumer.proxy;

import com.hao.rpc.consumer.transport.RpcClient;

import java.lang.reflect.Proxy;

public class ProxyFactory {

    private RpcClient client;

    public ProxyFactory(RpcClient client) {
        this.client = client;
    }

    // 可以设置一个缓存

    /**
     * 通过jdk动态代理方式创建clazz类型的实现类
     *
     */
    @SuppressWarnings("unchecked")
    public <T> T getProxy(Class<T> clazz) {
        return (T) Proxy.newProxyInstance(
                clazz.getClassLoader(),
                new Class<?>[]{clazz},
                new RequestHandler(client)
        );
    }

}
