package com.hao.rpc.test.server;

import com.hao.rpc.producer.registry.impl.DefaultServiceManager;
import com.hao.rpc.producer.transport.RpcServer;
import com.hao.rpc.producer.transport.impl.bio.BioRpcServer;

public class TestServer {

    public static void main(String[] args) {

        DefaultServiceManager serviceManager = new DefaultServiceManager();
        serviceManager.registerService(new HelloServiceImpl());
        serviceManager.registerService(new UserServiceImpl());

        RpcServer rpcServer = new BioRpcServer(serviceManager);
        rpcServer.exec(9000);

    }
}
