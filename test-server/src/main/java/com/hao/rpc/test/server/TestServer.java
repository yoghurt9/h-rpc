package com.hao.rpc.test.server;

import com.hao.rpc.producer.MethodExecutor;
import com.hao.rpc.registry.impl.DefaultServiceManager;

public class TestServer {

    public static void main(String[] args) {

        DefaultServiceManager serviceManager = new DefaultServiceManager();
        serviceManager.registerService(new HelloServiceImpl());
        serviceManager.registerService(new UserServiceImpl());

        MethodExecutor methodExecutor = new MethodExecutor(serviceManager);

        methodExecutor.exec(9000);

    }
}
