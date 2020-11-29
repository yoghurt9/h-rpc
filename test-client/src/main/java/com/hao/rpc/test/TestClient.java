package com.hao.rpc.test;

import com.hao.rpc.api.HelloObject;
import com.hao.rpc.api.HelloService;
import com.hao.rpc.consumer.ProxyFactory;

public class TestClient {
    public static void main(String[] args) {
        HelloService helloService = ProxyFactory.getProxy(
                HelloService.class, "localhost", 9000
        );

        HelloObject object = helloService.hello("张三", 22);
        System.out.println(object);
    }
}
