package com.hao.rpc.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 *  请求体
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
    private Class[] parameterTypes;

    /**
     *  实参
     */
    private Object[] parameters;

}
