package com.hao.rpc.registry.impl;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.hao.rpc.enumeration.RpcError;
import com.hao.rpc.exception.RpcException;
import com.hao.rpc.registry.ServiceRegistry;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.Set;

/**
 * Nacos服务注册中心
 *
 * 线程不安全，所以推荐在注册时采用单线程
 */
@Slf4j
public class NacosServiceRegistry implements ServiceRegistry {

    private final NamingService namingService;
    private final Set<String> registeredServices;
    private final String host;
    private final int port;

    /**
     *
     * @param host 本地ip, 也就是注册服务时传入的ip
     * @param port 本地端口, 也就是注册服务时传入的port
     * @param serverList nacos的 ip + port
     */
    public NacosServiceRegistry(String host, int port, String serverList) {
        try {
            namingService = NamingFactory.createNamingService(serverList);
        } catch (NacosException e) {
            log.error("连接到Nacos时有错误发生: ", e);
            throw new RpcException(RpcError.CONNECT_TO_SERVICE_REGISTRY_FAIL);
        }
        this.host = host;
        this.port = port;
        registeredServices = new HashSet<>();
    }

    @Override
    public void publish(String serviceName) {
        try {
            registeredServices.add(serviceName);
            namingService.registerInstance(serviceName, host, port);
        } catch (NacosException e) {
            log.error("注册服务时有错误发生:", e);
            throw new RpcException(RpcError.REGISTER_SERVICE_FAIL);
        }
    }

    /**
     * jvm关闭后，nacos不会自动注销服务，所以需要写一个钩子函数，来调用这个clear方法
     */
    public void clearRegistry() {
        if(!registeredServices.isEmpty()) {

            for (String serviceName : registeredServices) {
                try {
                    namingService.deregisterInstance(serviceName, host, port);
                } catch (NacosException e) {
                    log.error("注销服务 {} 失败", serviceName, e);
                }
            }
        }
    }
}