package com.hao.rpc.test.server;

import com.hao.rpc.producer.registry.impl.DefaultServiceManager;
import com.hao.rpc.producer.transport.RpcServer;
import com.hao.rpc.producer.transport.impl.bio.BioRpcServer;
import com.hao.rpc.test.server.impl.HelloServiceImpl;
import com.hao.rpc.test.server.impl.UserServiceImpl;

public class TestBioServer {

    public static void main(String[] args) {

        DefaultServiceManager serviceManager = new DefaultServiceManager();
        serviceManager.registerService(new HelloServiceImpl());
        serviceManager.registerService(new UserServiceImpl());

        RpcServer server = new BioRpcServer();
        server.exec(9000);
    }
}
