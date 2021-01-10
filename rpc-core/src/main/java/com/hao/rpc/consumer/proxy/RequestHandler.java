package com.hao.rpc.consumer.proxy;


import com.hao.rpc.consumer.transport.RpcClient;
import com.hao.rpc.entity.CodeMsg;
import com.hao.rpc.entity.RpcRequest;
import com.hao.rpc.entity.RpcResponse;
import com.hao.rpc.enums.RpcError;
import com.hao.rpc.exception.RpcException;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.text.MessageFormat;


/**
 * ProxyFactory创建代理类时, InvocationHandler的实现类
 */
@Slf4j
public class RequestHandler implements InvocationHandler {

    private RpcClient client;

    public RequestHandler(RpcClient client) {
        this.client = client;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        // method.getDeclaringClass() 返回的是 “实现了该方法” 的类
        // 比如子类重写了父类或者接口的方法，返回的就是子类;
        // 反之，只是继承但没重写，返回的就是父类

        // 1. 创建请求体
        RpcRequest rpcRequest = RpcRequest.builder()
                .interfaceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .parameterTypes(method.getParameterTypes())
                .parameters(args)
                .build();

        // 2. 通过rpcClient, 发送给生产者并获取response
        RpcResponse rpcResponse = client.sendRequest(rpcRequest);

        // 3. 检验 rpcResponse
        checkRpcResponse(rpcRequest, rpcResponse);

        // 4. 返回data
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
