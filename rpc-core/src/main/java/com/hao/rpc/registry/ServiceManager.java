package com.hao.rpc.registry;

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
