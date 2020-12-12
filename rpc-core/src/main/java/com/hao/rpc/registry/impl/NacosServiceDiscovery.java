package com.hao.rpc.registry.impl;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.hao.rpc.enumeration.RpcError;
import com.hao.rpc.exception.RpcException;
import com.hao.rpc.loadBalance.LoadBalancer;
import com.hao.rpc.registry.ServiceDiscovery;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class NacosServiceDiscovery implements ServiceDiscovery {

    private final NamingService namingService;
    private final LoadBalancer loadBalancer;

    public NacosServiceDiscovery(String serverList, LoadBalancer loadBalancer) {
        try {
            namingService = NamingFactory.createNamingService(serverList);
        } catch (NacosException e) {
            log.error("连接到Nacos时有错误发生: ", e);
            throw new RpcException(RpcError.CONNECT_TO_SERVICE_REGISTRY_FAIL);
        }
        this.loadBalancer = loadBalancer;
    }


    @Override
    public InetSocketAddress subscribe(String serviceName) {
        try {
            List<Instance> instances = namingService.getAllInstances(serviceName);
            if(instances.size() == 0) {
                log.error("找不到对应的服务: " + serviceName);
                throw new RpcException(RpcError.SERVICE_NOT_FOUND);
            }
            List<InetSocketAddress> inetList = toInetList(instances);
            return loadBalancer.select(inetList);
        } catch (NacosException e) {
            log.error("获取服务时有错误发生:", e);
        }
        return null;
    }

    private List<InetSocketAddress> toInetList(List<Instance> instances) {
        ArrayList<InetSocketAddress> inetList = new ArrayList<>();
        for (Instance instance : instances) {
            inetList.add(new InetSocketAddress(instance.getIp(), instance.getPort()));
        }
        return inetList;
    }

}
