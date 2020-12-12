package com.hao.rpc.registry.impl;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.hao.rpc.enumeration.RpcError;
import com.hao.rpc.exception.RpcException;
import com.hao.rpc.registry.ServiceDiscovery;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.List;

@Slf4j
public class NacosServiceDiscovery implements ServiceDiscovery {

    private final NamingService namingService;

    public NacosServiceDiscovery(String serverList) {
        try {
            namingService = NamingFactory.createNamingService(serverList);
        } catch (NacosException e) {
            log.error("连接到Nacos时有错误发生: ", e);
            throw new RpcException(RpcError.CONNECT_TO_SERVICE_REGISTRY_FAIL);
        }
    }


    @Override
    public InetSocketAddress subscribe(String serviceName) {
        try {
            List<Instance> instances = namingService.getAllInstances(serviceName);
            Instance instance = instances.get(0);
            return new InetSocketAddress(instance.getIp(), instance.getPort());
        } catch (NacosException e) {
            log.error("获取服务时有错误发生:", e);
        }
        return null;
    }

}
