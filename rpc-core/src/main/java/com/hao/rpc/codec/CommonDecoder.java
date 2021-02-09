package com.hao.rpc.codec;

import com.hao.rpc.entity.RpcRequest;
import com.hao.rpc.entity.RpcResponse;
import com.hao.rpc.enums.PackageType;
import com.hao.rpc.enums.RpcError;
import com.hao.rpc.exception.RpcException;
import com.hao.rpc.protocol.MsgHeader;
import com.hao.rpc.protocol.ProtocolConstants;
import com.hao.rpc.protocol.RpcProtocol;
import com.hao.rpc.serializer.RpcSerializer;
import com.hao.rpc.serializer.SerializerFactory;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 通用的解码拦截器
 */
@Slf4j
public class CommonDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (in.readableBytes() < ProtocolConstants.HEADER_TOTAL_LEN) {
            return;
        }

        in.markReaderIndex();

        // 1. 魔数
        short magic = in.readShort();
        if (magic != ProtocolConstants.MAGIC_NUMBER) {
            log.error(RpcError.UNKNOWN_PROTOCOL + " : {}", magic);
            throw new RpcException(RpcError.UNKNOWN_PROTOCOL);
        }

        // 2. 协议版本
        byte version = in.readByte();
        if (version != ProtocolConstants.VERSION) {
            log.error(RpcError.UNKNOWN_VERSION + " : {}", version);
            throw new RpcException(RpcError.UNKNOWN_VERSION);
        }

        // 3. 会话ID
        long sessionId = in.readLong();

        // 4. 报文类型
        byte msgType = in.readByte();
        Class<?> packageClass;
        if (msgType == PackageType.REQUEST_PACK.getCode()) {
            packageClass = RpcRequest.class;
        } else if (msgType == PackageType.RESPONSE_PACK.getCode()) {
            packageClass = RpcResponse.class;
        } else {
            log.error(RpcError.UNKNOWN_PACKAGE_TYPE + " : {}", msgType);
            throw new RpcException(RpcError.UNKNOWN_PACKAGE_TYPE);
        }

        // 5. 序列化器的类型
        byte serialization = in.readByte();
        RpcSerializer serializer = SerializerFactory.getSerializer(serialization);

        // 6. 数据长度
        int dataLen = in.readInt();
        if (in.readableBytes() < dataLen) {
            in.resetReaderIndex();
            return;
        }

        // 7. 读取数据并反序列化
        byte[] bytes = new byte[dataLen];
        in.readBytes(bytes);
        Object obj = serializer.deserialize(bytes, packageClass);

        // 封装Message
        MsgHeader header = new MsgHeader();
        header.setMagic(magic);
        header.setVersion(version);
        header.setSessionId(sessionId);
        header.setMsgType(msgType);
        header.setSerialization(serialization);
        header.setDataLen(dataLen);

        RpcProtocol<Object> protocol = new RpcProtocol<>();
        protocol.setHeader(header);
        protocol.setBody(obj);
        out.add(protocol);
    }

}
