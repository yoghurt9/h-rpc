package com.hao.rpc.test.server;

import com.hao.rpc.producer.registry.impl.DefaultServiceManager;
import com.hao.rpc.producer.transport.RpcServer;
import com.hao.rpc.producer.transport.impl.nio.NioRpcServer;
import com.hao.rpc.test.server.impl.HelloServiceImpl;
import com.hao.rpc.test.server.impl.UserServiceImpl;

/**
 * 测试用Netty服务提供者
 */
public class TestNioServer {

    public static void main(String[] args) {

        DefaultServiceManager serviceManager = new DefaultServiceManager();
        serviceManager.registerService(new HelloServiceImpl());
        serviceManager.registerService(new UserServiceImpl());

        RpcServer server = new NioRpcServer();
        server.exec(9001);
    }

}