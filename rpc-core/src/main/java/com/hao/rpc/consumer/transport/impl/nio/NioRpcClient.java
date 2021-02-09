package com.hao.rpc.consumer.transport.impl.nio;

import com.hao.rpc.consumer.transport.RpcClient;
import com.hao.rpc.entity.RpcRequest;
import com.hao.rpc.protocol.RpcProtocol;
import com.hao.rpc.registry.ServiceDiscovery;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

/**
 * NIO方式消费侧客户端类
 */
@Slf4j
public class NioRpcClient implements RpcClient {

    private ServiceDiscovery serviceDiscovery;

    public NioRpcClient(ServiceDiscovery serviceDiscovery) {
        this.serviceDiscovery = serviceDiscovery;
    }

    @Override
    public void sendRequest(RpcProtocol protocol) {
        // get() : 获取一个连接上远程主机的channel，现在的实现方式是：每一次调用get()都会对服务器发起一个新的连接
        RpcRequest request = (RpcRequest) protocol.getBody();
        InetSocketAddress inetSocketAddress = serviceDiscovery.discovery(request.getInterfaceName());
        Channel channel = ChannelProvider.get(inetSocketAddress);
        if(channel.isActive()) {
            channel.writeAndFlush(protocol).addListener(listener -> {
                if(listener.isSuccess()) {
                    log.debug("客户端发送消息成功: {}", protocol.toString());
                } else {
                    log.error("客户端发送消息失败: {}", listener.cause().toString());
                }
            });
        }
    }

}