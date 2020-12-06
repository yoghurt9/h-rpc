package com.hao.rpc.consumer.transport.impl.nio;

import com.hao.rpc.codec.CommonDecoder;
import com.hao.rpc.codec.CommonEncoder;
import com.hao.rpc.consumer.transport.RpcClient;
import com.hao.rpc.entity.RpcRequest;
import com.hao.rpc.entity.RpcResponse;
import com.hao.rpc.serializer.CommonSerializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;

/**
 * NIO方式消费侧客户端类
 */
@Slf4j
public class NioRpcClient implements RpcClient {

    private String host;
    private int port;
    private Bootstrap bootstrap;
    private CommonSerializer serializer;

    public NioRpcClient(String host, int port, CommonSerializer serializer) {
        this.host = host;
        this.port = port;
        this.serializer = serializer;
        init();
    }

    private void init() {
        bootstrap = new Bootstrap();
        bootstrap.group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new CommonDecoder())
                                .addLast(new CommonEncoder(serializer))
                                .addLast(new NioClientHandler());
                    }
                });
    }

    @Override
    public RpcResponse sendRequest(RpcRequest rpcRequest) {
        try {

            ChannelFuture future = bootstrap.connect(host, port).sync();
            log.info("客户端连接到服务器 {}:{}", host, port);
            Channel channel = future.channel();
            if(channel != null) {
                channel.writeAndFlush(rpcRequest).addListener(listener -> {
                    if(listener.isSuccess()) {
                        log.info("客户端发送消息成功: {}", rpcRequest.toString());
                    } else {
                        log.error("客户端发送消息失败: {}", listener.cause());
                    }
                });
                channel.closeFuture().sync();
                AttributeKey<RpcResponse> key = AttributeKey.valueOf("rpcResponse");
                return channel.attr(key).get();
            }

        } catch (InterruptedException e) {
            log.error("发送消息时有错误发生: ", e);
        }
        return null;
    }

    @Override
    public void setSerializer(CommonSerializer serializer) {

    }

}