package com.hao.rpc.registry;

/**
 * 发布/订阅中心, 和服务注册中心中间件交互的接口
 * publish/subscribe
 */
public interface ServiceRegistry {

    /**
     * 将一个服务注册进注册表
     *
     * @param serviceName 服务名称
     */
    void publish(String serviceName);

    /**
     *  清除本机之前在注册中心所注册的服务
     */
    void clearRegistry();
}
