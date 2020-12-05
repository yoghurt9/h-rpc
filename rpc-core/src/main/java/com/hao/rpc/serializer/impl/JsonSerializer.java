package com.hao.rpc.serializer.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hao.rpc.entity.RpcRequest;
import com.hao.rpc.enumeration.SerializerType;
import com.hao.rpc.serializer.CommonSerializer;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * 使用JSON格式的序列化器
 */
@Slf4j
public class JsonSerializer implements CommonSerializer {

    private ObjectMapper objectMapper = new ObjectMapper();


    @Override
    public byte[] serialize(Object obj) {
        try {

            return objectMapper.writeValueAsBytes(obj);
        } catch (JsonProcessingException e) {
            log.error("序列化时有错误发生: {}", e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Object deserialize(byte[] bytes, Class<?> clazz) {
        try {
            Object obj = objectMapper.readValue(bytes, clazz);
            if(obj instanceof RpcRequest) {
                obj = handleRequest(obj);
            }

            return obj;
        } catch (IOException e) {
            log.error("反序列化时有错误发生: {}", e.getMessage());
            e.printStackTrace();
            return null;
        }
    }


    /*
        这里由于使用JSON序列化和反序列化Object数组，无法保证反序列化后仍然为原实例类型
        需要重新判断处理
     */
    private Object handleRequest(Object obj) throws IOException {
        RpcRequest rpcRequest = (RpcRequest) obj;
        Class[] parameterTypes = rpcRequest.getParameterTypes();
        Object[] parameters = rpcRequest.getParameters();
        int n = parameters.length;
        Class<?> clazz;
        for(int i = 0; i < n; i++) {
            clazz = parameterTypes[i];
            if(!clazz.isAssignableFrom(parameters[i].getClass())) {
                // object ==> byte[]
                byte[] bytes = objectMapper.writeValueAsBytes(parameters[i]);
                // byte[] + clazz ==> object(clazz类型)
                parameters[i] = objectMapper.readValue(bytes, clazz);
            }
        }
        return rpcRequest;
    }

    @Override
    public int getCode() {
        return SerializerType.valueOf("JSON").getCode();
    }

}