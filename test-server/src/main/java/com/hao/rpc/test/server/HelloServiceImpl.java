package com.hao.rpc.test.server;

import com.hao.rpc.api.HelloObject;
import com.hao.rpc.api.HelloService;

public class HelloServiceImpl implements HelloService {
    @Override
    public HelloObject hello(String name, int age) {
        System.out.println("name: " + name + ", " + "age: " + age);
        return new HelloObject(name, age);
    }
}
