package com.hao.rpc.producer.transport.impl.nio;


import com.hao.rpc.entity.RpcRequest;
import com.hao.rpc.entity.RpcResponse;
import com.hao.rpc.producer.manager.ServiceManager;
import com.hao.rpc.producer.manager.impl.DefaultServiceManager;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


/**
 * Netty中处理RpcRequest的Handler
 */
@Slf4j
public class NioServerHandler extends SimpleChannelInboundHandler<RpcRequest> {

    private static ServiceManager serviceManager = new DefaultServiceManager();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequest rpcRequest) throws Exception {
            log.info("服务器接收到请求: {}", rpcRequest);

            // 1. 获取请求体
            String interfaceName = rpcRequest.getInterfaceName();
            Object service = serviceManager.getService(interfaceName);

            // 2. 反射调用方法
            Object result = invokeTargetMethod(rpcRequest, service);

            // 3. 把结果通过网络传输给消费端
            ChannelFuture future = ctx.writeAndFlush(RpcResponse.success(result));
            future.addListener(ChannelFutureListener.CLOSE_ON_FAILURE);

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("处理过程调用时有错误发生:");
        cause.printStackTrace();
        ctx.close();
    }


    private Object invokeTargetMethod(RpcRequest rpcRequest, Object service) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        Method method = service.getClass().getMethod(rpcRequest.getMethodName(), rpcRequest.getParameterTypes());
        return method.invoke(service, rpcRequest.getParameters());

    }

}
