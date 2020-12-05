package com.hao.rpc.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 响应体
 */
@Data
@NoArgsConstructor
public class RpcResponse<T> implements Serializable {

    private Integer code;
    private String msg;
    private T data;

    public static <S> RpcResponse<S> success(S data) {
        RpcResponse<S> rpcResponse = new RpcResponse<>();
        rpcResponse.setCode(CodeMsg.SUCCESS.code);
        rpcResponse.setMsg(CodeMsg.SUCCESS.msg);
        rpcResponse.setData(data);
        return rpcResponse;
    }

    public static <S> RpcResponse<S> fail() {
        RpcResponse<S> rpcResponse = new RpcResponse<>();
        rpcResponse.setCode(CodeMsg.FAIL.code);
        rpcResponse.setMsg(CodeMsg.FAIL.msg);
        return rpcResponse;
    }

}
