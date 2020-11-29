package com.hao.rpc.consumer;


import com.hao.rpc.entiry.RpcRequest;
import com.hao.rpc.entiry.RpcResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.Socket;

@Slf4j
public class RequestHandler implements InvocationHandler {

    private String host;
    private int port;

    public RequestHandler(String host, int port) {
        this.host = host;
        this.port = port;
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

        // 2. 发送给生产者并获取结果
        RpcResponse rpcResponse = (RpcResponse) sendRequest(rpcRequest);

        // 3. 返回结果
        return rpcResponse.getData();
    }

    public Object sendRequest(RpcRequest rpcRequest) {
        try (Socket socket = new Socket(host, port)) {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            objectOutputStream.writeObject(rpcRequest);
            objectOutputStream.flush();
            return objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            log.error("调用时有错误发生：", e);
            return null;
        }
    }
}
