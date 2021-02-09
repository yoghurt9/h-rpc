package com.hao.rpc.protocol;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * +----------------------------------------------------------------------------------------------------------------+
 * | 魔数 2byte | 协议版本号 1byte | 会话ID 8byte | 报文类型 1byte | 序列化类型 1byte | 数据长度 4byte | 数据内容(长度不定) |
 * +----------------------------------------------------------------------------------------------------------------+
 *
 * header_len = 17byte
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MsgHeader {

    /**
     * 魔数
     */
    private short magic;

    /**
     * 协议版本号
     */
    private byte version;

    /**
     * 会话ID
     */
    private long sessionId;

    /**
     * 报文类型
     */
    private byte msgType;

    /**
     * 序列化类型
     */
    private byte serialization;

    /**
     * 数据长度
     */
    private int dataLen;
}
