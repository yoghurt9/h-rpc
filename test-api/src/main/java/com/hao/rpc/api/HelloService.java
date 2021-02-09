package com.hao.rpc.api;

import java.util.concurrent.CompletableFuture;

public interface HelloService {
    HelloObject hello(String name, int age);

    default CompletableFuture<HelloObject> helloSync(String name, int age) {
        System.out.println("----异步方法-----");
        return CompletableFuture.completedFuture(hello(name, age));
    }
}
