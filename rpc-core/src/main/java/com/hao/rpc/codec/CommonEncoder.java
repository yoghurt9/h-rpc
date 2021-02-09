package com.hao.rpc.codec;

import com.hao.rpc.protocol.MsgHeader;
import com.hao.rpc.protocol.RpcProtocol;
import com.hao.rpc.serializer.RpcSerializer;
import com.hao.rpc.serializer.SerializerFactory;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;


/**
 * 通用的编码拦截器
 */
public class CommonEncoder extends MessageToByteEncoder<RpcProtocol<Object>> {

    public CommonEncoder() {}

    @Override
    protected void encode(ChannelHandlerContext ctx, RpcProtocol<Object> msg, ByteBuf out) throws Exception {
        MsgHeader header = msg.getHeader();
        out.writeShort(header.getMagic());
        out.writeByte(header.getVersion());
        out.writeLong(header.getSessionId());
        out.writeByte(header.getMsgType());
        out.writeByte(header.getSerialization());
        RpcSerializer serializer = SerializerFactory.getSerializer(header.getSerialization());
        byte[] data = serializer.serialize(msg.getBody());
        out.writeInt(data.length);
        out.writeBytes(data);
    }

}
