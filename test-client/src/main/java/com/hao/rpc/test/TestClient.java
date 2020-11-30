package com.hao.rpc.test;

import com.hao.rpc.api.HelloObject;
import com.hao.rpc.api.HelloService;
import com.hao.rpc.api.UserService;
import com.hao.rpc.consumer.ProxyFactory;

public class TestClient {
    public static void main(String[] args) {
        HelloService helloService = ProxyFactory.getProxy(
                HelloService.class, "localhost", 9000
        );

        UserService userService = ProxyFactory.getProxy(
                UserService.class, "localhost", 9000
        );

        String result1 = userService.login("张三");
        System.out.println(result1);


        HelloObject result2 = helloService.hello("张三", 22);
        System.out.println(result2);

    }
}
