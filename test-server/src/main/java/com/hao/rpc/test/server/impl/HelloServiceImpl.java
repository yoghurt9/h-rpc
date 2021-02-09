package com.hao.rpc.test.server.impl;

import com.hao.rpc.annotation.RpcService;
import com.hao.rpc.api.HelloObject;
import com.hao.rpc.api.HelloService;

@RpcService
public class HelloServiceImpl implements HelloService {
    @Override
    public HelloObject hello(String name, int age) {
        System.out.println("HelloServiceImpl === sleep 10s === name: " + name + ", " + "age: " + age);
//        try {
//            TimeUnit.SECONDS.sleep(10);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        return new HelloObject(name, age);
    }
}
