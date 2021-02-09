package com.hao.rpc.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 响应体
 */
@Data
@NoArgsConstructor
public class RpcResponse {

    private Integer code;
    private String msg;
    private Object data;

    public static RpcResponse success(Object data) {
        RpcResponse rpcResponse = new RpcResponse();
        rpcResponse.setCode(CodeMsg.SUCCESS.code);
        rpcResponse.setMsg(CodeMsg.SUCCESS.msg);
        rpcResponse.setData(data);
        return rpcResponse;
    }

    public static RpcResponse fail() {
        RpcResponse rpcResponse = new RpcResponse();
        rpcResponse.setCode(CodeMsg.FAIL.code);
        rpcResponse.setMsg(CodeMsg.FAIL.msg);
        return rpcResponse;
    }

}
