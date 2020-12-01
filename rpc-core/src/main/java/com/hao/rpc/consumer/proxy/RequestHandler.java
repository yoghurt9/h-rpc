package com.hao.rpc.consumer.proxy;


import com.hao.rpc.consumer.transport.RpcClient;
import com.hao.rpc.entiry.CodeMsg;
import com.hao.rpc.entiry.RpcRequest;
import com.hao.rpc.entiry.RpcResponse;
import com.hao.rpc.exception.RpcError;
import com.hao.rpc.exception.RpcException;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;


/**
 * ProxyFactory创建代理类时, InvocationHandler的实现类
 */
@Slf4j
public class RequestHandler implements InvocationHandler {

    private RpcClient rpcClient;

    public RequestHandler(RpcClient rpcClient) {
        this.rpcClient = rpcClient;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        // method.getDeclaringClass() 返回的是 “实现了该方法” 的类
        // 比如子类重写了父类或者接口的方法，返回的就是子类;
        // 反之，只是继承但没重写，返回的就是父类

        // 1. 创建请求体
        RpcRequest rpcRequest = RpcRequest.builder().interfaceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .parameterTypes(method.getParameterTypes())
                .parameters(args)
                .build();

        // 2. 通过rpcClient, 发送给生产者并获取response
        RpcResponse rpcResponse = rpcClient.sendRequest(rpcRequest);

        // 3. 检验 rpcResponse
        checkRpcResponse(rpcRequest, rpcResponse);

        // 4. 返回data
        return rpcResponse.getData();
    }


    private void checkRpcResponse(RpcRequest rpcRequest, RpcResponse rpcResponse) {
        if(rpcResponse == null) {
            log.error("rpcResponse is null，rpcRequest: {}", rpcRequest.toString());
            throw new RpcException(RpcError.SERVICE_INVOCATION_FAIL, "rpcResponse is null, rpcRequest: " + rpcRequest.toString());
        }
        if(rpcResponse.getCode() == null || !rpcResponse.getCode().equals(CodeMsg.SUCCESS.getCode())) {
            log.error("service invocation fail, rpcRequest: {}, rpcResponse:{}", rpcRequest.toString(), rpcResponse.toString());
            throw new RpcException(RpcError.SERVICE_INVOCATION_FAIL, "rpcRequest: " + rpcRequest.toString() + ", rpcResponse: " + rpcRequest.toString());
        }
    }

}
