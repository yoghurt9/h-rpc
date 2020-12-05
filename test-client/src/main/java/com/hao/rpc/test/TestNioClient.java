package com.hao.rpc.test;

import com.hao.rpc.api.HelloObject;
import com.hao.rpc.api.HelloService;
import com.hao.rpc.api.UserService;
import com.hao.rpc.consumer.proxy.ProxyFactory;
import com.hao.rpc.consumer.transport.impl.nio.NioRpcClient;

/**
 * 测试用Netty消费者
 */
public class TestNioClient {

    public static void main(String[] args) {
        NioRpcClient nioRpcClient = new NioRpcClient("127.0.0.1", 9001);
        ProxyFactory proxyFactory = new ProxyFactory(nioRpcClient);

        HelloService helloService = proxyFactory.getProxy(HelloService.class);
        UserService userService = proxyFactory.getProxy(UserService.class);

        String result1 = userService.login("张三");
        System.out.println(result1);


        HelloObject result2 = helloService.hello("张三", 22);
        System.out.println(result2);
    }

}