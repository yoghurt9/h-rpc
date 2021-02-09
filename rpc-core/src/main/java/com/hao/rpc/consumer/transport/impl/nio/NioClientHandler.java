package com.hao.rpc.consumer.transport.impl.nio;

import com.hao.rpc.entity.RpcResponse;
import com.hao.rpc.protocol.RpcProtocol;
import com.hao.rpc.protocol.RpcSessionHolder;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;

/**
 * Netty客户端侧处理器
 */
@Slf4j
public class NioClientHandler extends SimpleChannelInboundHandler<RpcProtocol<RpcResponse>> {


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcProtocol<RpcResponse> msg) {
        long sessionId = msg.getHeader().getSessionId();
        CompletableFuture<RpcResponse> future = RpcSessionHolder.removeSession(sessionId);
        future.complete(msg.getBody());
        ctx.channel().close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("过程调用时有错误发生:");
        cause.printStackTrace();
        ctx.close();
    }
}