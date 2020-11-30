package com.hao.rpc.producer;

import com.hao.rpc.entiry.RpcRequest;
import com.hao.rpc.entiry.RpcResponse;
import com.hao.rpc.registry.ServiceManager;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;

@Slf4j
public class MethodTask implements Runnable {

    private Socket socket;
    private ServiceManager serviceManager;

    public MethodTask(Socket socket, ServiceManager serviceManager) {
        this.socket = socket;
        this.serviceManager = serviceManager;
    }

    @Override
    public void run() {
        try (ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream())) {

            // 获取请求体
            RpcRequest rpcRequest = (RpcRequest) objectInputStream.readObject();

            // 获取服务实体
            String interfaceName = rpcRequest.getInterfaceName();
            Object service = serviceManager.getService(interfaceName);

            // 反射调用方法
            Object result = invokeTargetMethod(rpcRequest, service);

            // 把结果通过网络传输给消费端
            objectOutputStream.writeObject(RpcResponse.success(result));
            objectOutputStream.flush();

        } catch (IOException | ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            log.error("provider execute method error: ", e);
        }
    }

    private Object invokeTargetMethod(RpcRequest rpcRequest, Object service) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        Method method = service.getClass().getMethod(rpcRequest.getMethodName(), rpcRequest.getParameterTypes());
        return method.invoke(service, rpcRequest.getParameters());

    }
}
