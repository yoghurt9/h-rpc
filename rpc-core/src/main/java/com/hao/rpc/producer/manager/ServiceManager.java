package com.hao.rpc.producer.manager;

public interface ServiceManager {

    /**
     * 将本地的服务实现存储在map中
     *
     * @param service 接口的实现类
     * @param serviceName 接口的全限定名
     * @param <T> 接口类型
     */
    <T> void addService(T service, String serviceName);

    /**
     * 根据服务名称获取服务实体
     * @param serviceName 接口名
     * @return 具体的实现类
     */
    Object getService(String serviceName);

}
