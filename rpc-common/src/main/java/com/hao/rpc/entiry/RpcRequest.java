package com.hao.rpc.entiry;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 *  请求体
 */
@Data
@Builder
public class RpcRequest implements Serializable {

    /**
     * 调用接口名
     */
    private String interfaceName;

    /**
     *  调用方法名
     */
    private String methodName;

    /**
     *  形参列表
     */
    private       Class[] parameterTypes;

    /**
     *  实参
     */
    private Object[] parameters;

}
