package com.hao.rpc.protocol;

import lombok.Data;

@Data
public class RpcProtocol<T> {

    /**
     * 消息头
     */
    private MsgHeader header;

    /**
     * 消息体
     */
    private T body;
}
