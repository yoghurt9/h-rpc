package com.hao.rpc.serializer;

import com.hao.rpc.serializer.impl.HessianSerializer;
import com.hao.rpc.serializer.impl.JsonSerializer;
import com.hao.rpc.serializer.impl.KryoSerializer;
import com.hao.rpc.serializer.impl.ProtobufSerializer;

/**
 * 通用的序列化反序列化接口, 用来给编解码器使用
 */
public interface CommonSerializer {

    byte[] serialize(Object obj);

    Object deserialize(byte[] bytes, Class<?> clazz);

    int getCode();

    static CommonSerializer getByCode(int code) {
        switch (code) {
            case 0:
                return new KryoSerializer();
            case 1:
                return new ProtobufSerializer();
            case 2:
                return new JsonSerializer();
            case 3:
                return new HessianSerializer();
            default:
                return null;
        }
    }

}
