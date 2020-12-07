package com.hao.rpc.consumer.transport.impl.nio;

import com.hao.rpc.consumer.transport.RpcClient;
import com.hao.rpc.entity.RpcRequest;
import com.hao.rpc.entity.RpcResponse;
import com.hao.rpc.serializer.CommonSerializer;
import io.netty.channel.Channel;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.concurrent.atomic.AtomicReference;

/**
 * NIO方式消费侧客户端类
 */
@Slf4j
public class NioRpcClient implements RpcClient {

    private String host;
    private int port;
    private CommonSerializer serializer;

    public NioRpcClient(String host, int port, CommonSerializer serializer) {
        this.host = host;
        this.port = port;
        this.serializer = serializer;
    }



    @Override
    public RpcResponse sendRequest(RpcRequest rpcRequest) {
        AtomicReference<Object> result = new AtomicReference<>(null);
        try {
            // get() : 获取一个连接上远程主机的channel，现在的实现方式是：每一次调用get()都会对服务器发起一个新的连接
            Channel channel = ChannelProvider.get(new InetSocketAddress(host, port), serializer);
            if(channel.isActive()) {
                channel.writeAndFlush(rpcRequest).addListener(listener -> {
                    if(listener.isSuccess()) {
                        log.info("客户端发送消息成功: {}", rpcRequest.toString());
                    } else {
                        log.error("客户端发送消息失败: {}", listener.cause());
                    }
                });
                channel.closeFuture().sync();
                AttributeKey<RpcResponse> key = AttributeKey.valueOf("rpcResponse");
                RpcResponse rpcResponse = channel.attr(key).get();
                result.set(rpcResponse);
            }
        } catch (InterruptedException e) {
            log.error("发送消息时有错误发生: ", e);
        }
        return (RpcResponse) result.get();
    }

    @Override
    public void setSerializer(CommonSerializer serializer) {
        this.serializer = serializer;
    }

}