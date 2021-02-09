package com.hao.rpc.consumer.transport;

import com.hao.rpc.protocol.RpcProtocol;

public interface RpcClient {
    void sendRequest(RpcProtocol protocol);
}
