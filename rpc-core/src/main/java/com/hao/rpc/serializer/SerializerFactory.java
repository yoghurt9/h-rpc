package com.hao.rpc.serializer;

import com.hao.rpc.serializer.impl.HessianSerializer;
import com.hao.rpc.serializer.impl.JsonSerializer;
import com.hao.rpc.serializer.impl.KryoSerializer;
import com.hao.rpc.serializer.impl.ProtobufSerializer;

public class SerializerFactory {

    public static final byte KRYO = 0;
    public static final byte PROTOBUF = 1;
    public static final byte JSON = 2;
    public static final byte HESSIAN = 3;

    public static RpcSerializer getSerializer(byte code) {
        switch (code) {
            case KRYO:
                return new KryoSerializer();
            case JSON:
                return new JsonSerializer();
            case HESSIAN:
                return new HessianSerializer();
            default:
                return new ProtobufSerializer();
        }
    }
}
