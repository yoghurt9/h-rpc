package com.hao.rpc.producer.transport;

import com.hao.rpc.serializer.CommonSerializer;

public interface RpcServer {
    void exec(int port);

    void setSerializer(CommonSerializer serializer);
}
