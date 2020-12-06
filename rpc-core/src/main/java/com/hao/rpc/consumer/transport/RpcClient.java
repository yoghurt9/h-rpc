package com.hao.rpc.consumer.transport;

import com.hao.rpc.entity.RpcRequest;
import com.hao.rpc.entity.RpcResponse;
import com.hao.rpc.serializer.CommonSerializer;

public interface RpcClient {
    RpcResponse sendRequest(RpcRequest rpcRequest);

    void setSerializer(CommonSerializer serializer);

}
