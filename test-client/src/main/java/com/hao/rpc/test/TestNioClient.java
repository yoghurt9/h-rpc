package com.hao.rpc.test;

import com.hao.rpc.api.HelloObject;
import com.hao.rpc.api.HelloService;
import com.hao.rpc.api.UserService;
import com.hao.rpc.consumer.proxy.ProxyFactory;
import com.hao.rpc.consumer.transport.impl.nio.NioRpcClient;
import com.hao.rpc.loadBalance.impl.RoundRobinLoadBalancer;
import com.hao.rpc.registry.impl.NacosServiceDiscovery;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * 测试用Netty消费者
 */
public class TestNioClient {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        NacosServiceDiscovery nacosServiceDiscovery = new NacosServiceDiscovery("127.0.0.1:8848", new RoundRobinLoadBalancer());
        NioRpcClient nioRpcClient = new NioRpcClient(nacosServiceDiscovery);
        ProxyFactory proxyFactory = new ProxyFactory(nioRpcClient);

        HelloService helloService = proxyFactory.getProxy(HelloService.class);
        UserService userService = proxyFactory.getProxy(UserService.class);

        long start, end;
//        start = System.currentTimeMillis();
//        String result1 = userService.login("张三");
//        System.out.println(result1);
//        end = System.currentTimeMillis();
//        System.out.println("调用时长: " + (end -start) + "毫秒");
//
//
        start = System.currentTimeMillis();
        HelloObject result2 = helloService.hello("张三", 22);
        System.out.println(result2);
        end = System.currentTimeMillis();
        System.out.println("调用时长: " + (end -start) + "毫秒");
//
//        start = System.currentTimeMillis();
//        String result3 = userService.login("张三");
//        System.out.println(result3);
//        end = System.cur7rentTimeMillis();
//        System.out.println("调用时长: " + (end -start) + "毫秒");
//
//
//        System.out.println("================ 异步future ==============");
        start = System.currentTimeMillis();
        CompletableFuture<HelloObject> future = helloService.helloSync("异步张三", 56);
        // 为Future添加回调
        System.out.println(future.get());
        future.whenCompleteAsync((retValue, exception) -> {
            System.out.println("再执行这个22222");
            if (exception == null) {
                System.out.println(retValue);
            } else {
                exception.printStackTrace();
            }
        });
        System.out.println("先执行这个11111");
        end = System.currentTimeMillis();
        System.out.println("调用时长: " + (end -start) + "毫秒");
    }
}
