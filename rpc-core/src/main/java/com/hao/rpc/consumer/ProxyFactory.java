package com.hao.rpc.consumer;

import java.lang.reflect.Proxy;

public class ProxyFactory {

    // 可以设置一个缓存
    @SuppressWarnings("unchecked")
    public static <T> T getProxy(Class<T> clazz, String host, int port) {
        return (T) Proxy.newProxyInstance(
                clazz.getClassLoader(),
                new Class<?>[]{clazz},
                new RequestHandler(host, port)
        );
    }

}
