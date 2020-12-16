package com.hao.rpc.serializer;

/**
 * 通用的序列化反序列化接口, 用来给编解码器使用
 */
public interface CommonSerializer {

    byte[] serialize(Object obj);

    Object deserialize(byte[] bytes, Class<?> clazz);

    int getCode();

}
