package com.hao.rpc.test;

import com.hao.rpc.api.HelloObject;
import com.hao.rpc.api.HelloService;
import com.hao.rpc.api.UserService;
import com.hao.rpc.consumer.proxy.ProxyFactory;
import com.hao.rpc.consumer.transport.impl.bio.BioRpcClient;

public class TestBioClient {
    public static void main(String[] args) {
        BioRpcClient bioRpcClient = new BioRpcClient("127.0.0.1", 9000);
        ProxyFactory proxyFactory = new ProxyFactory(bioRpcClient);

        HelloService helloService = proxyFactory.getProxy(HelloService.class);
        UserService userService = proxyFactory.getProxy(UserService.class);

        String result1 = userService.login("张三");
        System.out.println(result1);


        HelloObject result2 = helloService.hello("张三", 22);
        System.out.println(result2);
    }
}
