package com.hao.rpc.producer.transport;

import com.hao.rpc.serializer.CommonSerializer;

public interface RpcServer {

    /**
     * 启动服务器，可以开始接收客户端连接
     *
     */
    void exec(Class<?> clazz);

    /**
     * 设置传输时的序列化器
     * @param serializer 序列化器的实现类
     */
    void setSerializer(CommonSerializer serializer);


    /**
     * 将服务保存在本地的注册表，同时注册到Nacos上
     *
     * @param service 服务的实现类
     * @param serviceName 服务接口的Class对象名
     */
    void register(Object service, String serviceName);
}
