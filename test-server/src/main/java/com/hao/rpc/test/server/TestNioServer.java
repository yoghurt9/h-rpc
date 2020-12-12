package com.hao.rpc.test.server;

import com.hao.rpc.api.HelloService;
import com.hao.rpc.api.UserService;
import com.hao.rpc.hook.ShutDownHook;
import com.hao.rpc.producer.transport.RpcServer;
import com.hao.rpc.producer.transport.impl.nio.NioRpcServer;
import com.hao.rpc.registry.impl.NacosServiceRegistry;
import com.hao.rpc.serializer.impl.KryoSerializer;
import com.hao.rpc.test.server.impl.HelloServiceImpl;
import com.hao.rpc.test.server.impl.UserServiceImpl;

/**
 * 测试用Netty服务提供者
 */
public class TestNioServer {

    public static void main(String[] args) {
        NacosServiceRegistry nacosServiceRegistry = new NacosServiceRegistry("127.0.0.1", 9999, "127.0.0.1:8848");
        RpcServer server = new NioRpcServer("127.0.0.1",
                9999,
                nacosServiceRegistry,
                new KryoSerializer());

        server.register(new HelloServiceImpl(), HelloService.class);
        server.register(new UserServiceImpl(), UserService.class);

        new ShutDownHook(nacosServiceRegistry).addClearTask();

        server.exec();
    }

}