package com.hao.rpc.test.server;

import com.hao.rpc.api.HelloService;
import com.hao.rpc.api.UserService;
import com.hao.rpc.producer.manager.impl.DefaultServiceManager;
import com.hao.rpc.producer.transport.RpcServer;
import com.hao.rpc.producer.transport.impl.bio.BioRpcServer;
import com.hao.rpc.test.server.impl.HelloServiceImpl;
import com.hao.rpc.test.server.impl.UserServiceImpl;

public class TestBioServer {

    public static void main(String[] args) {

        DefaultServiceManager serviceManager = new DefaultServiceManager();
        serviceManager.addService(new HelloServiceImpl(), HelloService.class.getCanonicalName());
        serviceManager.addService(new UserServiceImpl(), UserService.class.getCanonicalName());

        RpcServer server = new BioRpcServer(9999);
        server.exec();

    }
}
