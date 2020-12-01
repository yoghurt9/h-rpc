package com.hao.rpc.producer.registry;

public interface ServiceManager {

    /**
     * 注册服务
     * @param service
     * @param <T>
     */
    <T> void registerService(T service);

    /**
     * 根据服务名称获取服务实体
     * @param serviceName
     * @return
     */
    Object getService(String serviceName);

}
