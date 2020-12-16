package com.hao.rpc.codec;

import com.hao.rpc.entity.RpcRequest;
import com.hao.rpc.enums.PackageType;
import com.hao.rpc.serializer.CommonSerializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;


/**
 * 通用的编码拦截器
 */
public class CommonEncoder extends MessageToByteEncoder {

    private static final int MAGIC_NUMBER = 0xCAFEBABE;

    private final CommonSerializer serializer;

    public CommonEncoder(CommonSerializer serializer) {
        this.serializer = serializer;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        // 1. 魔数
        out.writeInt(MAGIC_NUMBER);

        // 2. 包的类型
        if(msg instanceof RpcRequest) {
            out.writeInt(PackageType.REQUEST_PACK.getCode());
        } else {
            out.writeInt(PackageType.RESPONSE_PACK.getCode());
        }

        // 3. 序列化器的类型
        out.writeInt(serializer.getCode());

        // 4. 序列化并获取长度
        byte[] bytes = serializer.serialize(msg);
        out.writeInt(bytes.length);

        // 5. 数据
        out.writeBytes(bytes);
    }

}
