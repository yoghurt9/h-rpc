package com.hao.rpc.test.server;

import com.hao.rpc.annotation.ServiceScan;
import com.hao.rpc.hook.ShutDownHook;
import com.hao.rpc.producer.transport.RpcServer;
import com.hao.rpc.producer.transport.impl.nio.NioRpcServer;
import com.hao.rpc.registry.impl.NacosServiceRegistry;
import com.hao.rpc.serializer.impl.KryoSerializer;

/**
 * 测试用Netty服务提供者
 */
@ServiceScan
public class TestNioServer {

    public static void main(String[] args) {
        NacosServiceRegistry nacosServiceRegistry = new NacosServiceRegistry("127.0.0.1", 9999, "127.0.0.1:8848");
        RpcServer server = new NioRpcServer("127.0.0.1",
                9999,
                nacosServiceRegistry,
                new KryoSerializer());

        new ShutDownHook(nacosServiceRegistry).addClearTask();

        server.exec(TestNioServer.class);
    }

}