package com.hao.rpc.consumer.proxy;


import com.hao.rpc.consumer.transport.RpcClient;
import com.hao.rpc.entity.CodeMsg;
import com.hao.rpc.entity.RpcRequest;
import com.hao.rpc.entity.RpcResponse;
import com.hao.rpc.enums.PackageType;
import com.hao.rpc.enums.RpcError;
import com.hao.rpc.exception.RpcException;
import com.hao.rpc.protocol.MsgHeader;
import com.hao.rpc.protocol.ProtocolConstants;
import com.hao.rpc.protocol.RpcProtocol;
import com.hao.rpc.protocol.RpcSessionHolder;
import com.hao.rpc.serializer.SerializerFactory;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;


/**
 * ProxyFactory创建代理类时, InvocationHandler的实现类
 */
@Slf4j
public class RequestHandler implements InvocationHandler {

    private final RpcClient client;

    public RequestHandler(RpcClient client) {
        this.client = client;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {

        // 1. 封装消息头
        long sessionId = RpcSessionHolder.getSessionId();
        MsgHeader header = MsgHeader.builder()
                .magic(ProtocolConstants.MAGIC_NUMBER)
                .version(ProtocolConstants.VERSION)
                .sessionId(sessionId)
                .msgType(PackageType.REQUEST_PACK.getCode())
                .serialization(SerializerFactory.PROTOBUF)
                .build();

        // method.getDeclaringClass() 返回的是 “实现了该方法” 的类
        // 比如子类重写了父类或者接口的方法，返回的就是子类;
        // 反之，只是继承但没重写，返回的就是父类
        // 2. 创建消息体
        RpcRequest rpcRequest = RpcRequest.builder()
                .interfaceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .parameterTypes(method.getParameterTypes())
                .parameters(args)
                .build();

        // 3. 封装协议
        RpcProtocol<RpcRequest> protocol = new RpcProtocol<>();
        protocol.setHeader(header);
        protocol.setBody(rpcRequest);

        CompletableFuture<RpcResponse> future = new CompletableFuture<>();
        RpcSessionHolder.addSession(sessionId, future);

        // 2. 通过rpcClient, 发送给生产者
        client.sendRequest(protocol);

        // 3. 如果是异步，在发送完成后，就可以返回了
        if (method.getReturnType().equals(CompletableFuture.class)) {
            return future;
        }

        // 4. 同步获取 rpcResponse
        RpcResponse rpcResponse = null;
        try {
            rpcResponse = future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        checkRpcResponse(rpcRequest, rpcResponse);

        // 5. 返回data
        return rpcResponse.getData();
    }


    private void checkRpcResponse(RpcRequest rpcRequest, RpcResponse rpcResponse) {
        String msg;
        if(rpcResponse == null) {
            msg = MessageFormat.format("rpcResponse is null，rpcRequest: {0}", rpcRequest.toString());
            log.error(msg);
            throw new RpcException(RpcError.SERVICE_INVOCATION_FAIL, msg);
        }

        if (!CodeMsg.SUCCESS.getCode().equals(rpcResponse.getCode())) {
            msg = MessageFormat.format("service invocation fail, rpcRequest: {0}, rpcResponse:{1}", rpcRequest.toString(), rpcRequest.toString());
            log.error(msg);
            throw new RpcException(RpcError.SERVICE_INVOCATION_FAIL, msg);
        }
    }

}
