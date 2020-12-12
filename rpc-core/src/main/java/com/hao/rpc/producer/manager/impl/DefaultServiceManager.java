package com.hao.rpc.producer.manager.impl;

import com.hao.rpc.enumeration.RpcError;
import com.hao.rpc.exception.RpcException;
import com.hao.rpc.producer.manager.ServiceManager;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 存储本地服务的容器
 */
@Slf4j
public class DefaultServiceManager implements ServiceManager {

    /**
     * 把这个map设置为静态的，那么每一个实例都会共享这个map
     * key 是服务名(接口全限定名), value 是实现类实体
     */
    private static Map<String, Object> serviceMap = new ConcurrentHashMap<>();


    /**
     * 将本地的服务实现存储在map中
     *
     * @param service 接口的实现类
     * @param serviceName 接口的全限定名
     * @param <T> 接口类型
     */
    @Override
    public <T> void addService(T service, String serviceName) {
        if (serviceMap.containsKey(serviceName)) {
            return;
        }

        serviceMap.put(serviceName, service);

        log.info("向接口: {} 注册服务: {}", serviceName, service.getClass().getName());
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
