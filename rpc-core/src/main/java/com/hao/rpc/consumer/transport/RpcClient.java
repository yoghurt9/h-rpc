package com.hao.rpc.consumer.transport;

import com.hao.rpc.entiry.RpcRequest;
import com.hao.rpc.entiry.RpcResponse;

public interface RpcClient {
    RpcResponse sendRequest(RpcRequest rpcRequest);
}
