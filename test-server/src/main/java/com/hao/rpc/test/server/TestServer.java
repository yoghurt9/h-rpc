package com.hao.rpc.test.server;

import com.hao.rpc.producer.MethodExecutor;

public class TestServer {

    public static void main(String[] args) {

        MethodExecutor methodExecutor = new MethodExecutor();

        methodExecutor.register(new DefaultService(), 9000);

    }
}
