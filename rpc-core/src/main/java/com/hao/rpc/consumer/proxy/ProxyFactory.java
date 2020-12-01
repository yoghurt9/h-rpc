package com.hao.rpc.consumer.proxy;

import com.hao.rpc.consumer.transport.RpcClient;

import java.lang.reflect.Proxy;

public class ProxyFactory {

    private RpcClient rpcClient;

    public ProxyFactory(RpcClient rpcClient) {
        this.rpcClient = rpcClient;
    }

    // 可以设置一个缓存
    @SuppressWarnings("unchecked")
    public <T> T getProxy(Class<T> clazz) {
        return (T) Proxy.newProxyInstance(
                clazz.getClassLoader(),
                new Class<?>[]{clazz},
                new RequestHandler(rpcClient)
        );
    }

}
