package com.hao.rpc.serializer;

import com.hao.rpc.serializer.impl.JsonSerializer;

/**
 * 通用的序列化反序列化接口, 用来给编解码器使用
 */
public interface CommonSerializer {

    byte[] serialize(Object obj);

    Object deserialize(byte[] bytes, Class<?> clazz);

    int getCode();

    static CommonSerializer getByCode(int code) {
        switch (code) {
            case 1:
                return new JsonSerializer();
            default:
                return null;
        }
    }

}
