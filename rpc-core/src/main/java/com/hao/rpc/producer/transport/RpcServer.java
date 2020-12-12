package com.hao.rpc.producer.transport;

import com.hao.rpc.serializer.CommonSerializer;

public interface RpcServer {

    /**
     * 启动服务器，可以开始接收客户端连接
     *
     */
    void exec();

    /**
     * 设置传输时的序列化器
     * @param serializer 序列化器的实现类
     */
    void setSerializer(CommonSerializer serializer);


    /**
     * 将服务保存在本地的注册表，同时注册到Nacos上
     *
     * @param service 服务的实现类
     * @param serviceClass 服务接口的Class对象
     * @param <T> 服务接口类型
     */
    <T> void register(T service, Class<T> serviceClass);
}
