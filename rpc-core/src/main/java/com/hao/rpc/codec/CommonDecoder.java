package com.hao.rpc.codec;

import com.hao.rpc.entity.RpcRequest;
import com.hao.rpc.entity.RpcResponse;
import com.hao.rpc.enums.PackageType;
import com.hao.rpc.enums.RpcError;
import com.hao.rpc.exception.RpcException;
import com.hao.rpc.serializer.CommonSerializer;
import com.hao.rpc.serializer.SerializerFactory;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 通用的解码拦截器
 */
@Slf4j
public class CommonDecoder extends ReplayingDecoder {

    private static final int MAGIC_NUMBER = 0xCAFEBABE;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        // 1. 魔数
        int magic = in.readInt();
        if(magic != MAGIC_NUMBER) {
            log.error(RpcError.UNKNOWN_PROTOCOL + " : {}", magic);
            throw new RpcException(RpcError.UNKNOWN_PROTOCOL);
        }

        // 2. 包的类型
        int packageCode = in.readInt();
        Class<?> packageClass;
        if(packageCode == PackageType.REQUEST_PACK.getCode()) {
            packageClass = RpcRequest.class;
        } else if(packageCode == PackageType.RESPONSE_PACK.getCode()) {
            packageClass = RpcResponse.class;
        } else {
            log.error(RpcError.UNKNOWN_PACKAGE_TYPE + " : {}", packageCode);
            throw new RpcException(RpcError.UNKNOWN_PACKAGE_TYPE);
        }

        // 3. 序列化器的类型
        int serializerCode = in.readInt();
        CommonSerializer serializer = SerializerFactory.getSerializer(serializerCode);
        if(serializer == null) {
            log.error(RpcError.UNKNOWN_SERIALIZER + " : {}", serializerCode);
            throw new RpcException(RpcError.UNKNOWN_SERIALIZER);
        }

        // 4. 数据长度
        int length = in.readInt();

        // 5. 读取数据
        byte[] bytes = new byte[length];
        in.readBytes(bytes);

        // 6. 反序列化
        Object obj = serializer.deserialize(bytes, packageClass);
        out.add(obj);
    }

}
