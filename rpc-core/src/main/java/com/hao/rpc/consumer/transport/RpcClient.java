package com.hao.rpc.consumer.transport;

import com.hao.rpc.entity.RpcRequest;
import com.hao.rpc.entity.RpcResponse;

public interface RpcClient {
    RpcResponse sendRequest(RpcRequest rpcRequest);
}
