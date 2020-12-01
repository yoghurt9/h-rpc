package com.hao.rpc.producer.registry.impl;

import com.hao.rpc.exception.RpcError;
import com.hao.rpc.exception.RpcException;
import com.hao.rpc.producer.registry.ServiceManager;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class DefaultServiceManager implements ServiceManager {

    private Map<String, Object> serviceMap = new ConcurrentHashMap<>();
    private Set<String> registeredService = ConcurrentHashMap.newKeySet();

    @Override
    public <T> void registerService(T service) {
        String serviceName = service.getClass().getCanonicalName();// 看看netty的那个
        if (registeredService.contains(serviceName)) {
            return;
        }

        registeredService.add(serviceName);

        Class<?>[] interfaces = service.getClass().getInterfaces();

        if(interfaces.length == 0) {
            throw new RpcException(RpcError.SERVICE_NOT_IMPLEMENT_ANY_INTERFACE, serviceName);
        }

        for(Class<?> i : interfaces) {
            serviceMap.put(i.getCanonicalName(), service);
        }

        log.info("向接口: {} 注册服务: {}", interfaces, serviceName);
    }

    @Override
    public Object getService(String serviceName) {
        Object service = serviceMap.get(serviceName);
        if (null == service) {
            throw new RpcException(RpcError.SERVICE_NOT_FOUND, serviceName);
        }

        return service;
    }

}
