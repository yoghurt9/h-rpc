package com.hao.rpc.producer.transport.impl.nio;


import com.hao.rpc.entity.RpcRequest;
import com.hao.rpc.entity.RpcResponse;
import com.hao.rpc.enums.PackageType;
import com.hao.rpc.producer.manager.ServiceManager;
import com.hao.rpc.producer.manager.impl.DefaultServiceManager;
import com.hao.rpc.protocol.MsgHeader;
import com.hao.rpc.protocol.RpcProtocol;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


/**
 * Netty中处理RpcRequest的Handler
 */
@Slf4j
public class NioServerHandler extends SimpleChannelInboundHandler<RpcProtocol<RpcRequest>> {

    private static final ServiceManager SERVICE_MANAGER = DefaultServiceManager.INSTANCE;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcProtocol<RpcRequest> msg) throws Exception {
        RpcRequestProcessor.submitRequest(() -> {
            RpcProtocol<RpcResponse> resProtocol = new RpcProtocol<>();
            MsgHeader header = msg.getHeader();
            header.setMsgType(PackageType.RESPONSE_PACK.getCode());
            RpcResponse response;
            try {
//                Object result = handle(msg.getBody());
                Object result = invokeTargetMethod(msg.getBody());
                response = RpcResponse.success(result);
            } catch (Throwable throwable) {
                response = RpcResponse.fail();
                log.error("process request {} error, {}", header.getSessionId(), throwable);
            }

            resProtocol.setHeader(header);
            resProtocol.setBody(response);
            ctx.writeAndFlush(resProtocol);
        });

    }

//    private Object handle(RpcRequest request) throws Throwable {
//        String serviceKey = RpcServiceHelper.buildServiceKey(request.getClassName(), request.getServiceVersion());
//        Object serviceBean = rpcServiceMap.get(serviceKey);
//
//        if (serviceBean == null) {
//            throw new RuntimeException(String.format("service not exist: %s:%s", request.getClassName(), request.getMethodName()));
//        }
//
//        Class<?> serviceClass = serviceBean.getClass();
//        String methodName = request.getMethodName();
//        Class<?>[] parameterTypes = request.getParameterTypes();
//        Object[] parameters = request.getParams();
//
//        FastClass fastClass = FastClass.create(serviceClass);
//        int methodIndex = fastClass.getIndex(methodName, parameterTypes);
//        return fastClass.invoke(methodIndex, serviceBean, parameters);
//    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("处理过程调用时有错误发生:");
        cause.printStackTrace();
        ctx.close();
    }


    private Object invokeTargetMethod(RpcRequest rpcRequest) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String interfaceName = rpcRequest.getInterfaceName();
        Object service = SERVICE_MANAGER.getService(interfaceName);
        Method method = service.getClass().getMethod(rpcRequest.getMethodName(), rpcRequest.getParameterTypes());
        return method.invoke(service, rpcRequest.getParameters());

    }

}
