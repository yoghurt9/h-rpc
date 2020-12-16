package com.hao.rpc.serializer;

import com.hao.rpc.serializer.impl.HessianSerializer;
import com.hao.rpc.serializer.impl.JsonSerializer;
import com.hao.rpc.serializer.impl.KryoSerializer;
import com.hao.rpc.serializer.impl.ProtobufSerializer;

public class SerializerFactory {

    public static final int KRYO = 0;
    public static final int PROTOBUF = 1;
    public static final int JSON = 2;
    public static final int HESSIAN = 3;

    public static CommonSerializer getSerializer(int code) {
        switch (code) {
            case KRYO:
                return new KryoSerializer();
            case PROTOBUF:
                return new ProtobufSerializer();
            case JSON:
                return new JsonSerializer();
            case HESSIAN:
                return new HessianSerializer();
            default:
                return null;
        }
    }
}
