package com.hao.rpc.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * RPC调用过程中的错误
 */
@AllArgsConstructor
@Getter
public enum RpcError {

    SERVICE_INVOCATION_FAIL("service invocation fail"),
    SERVICE_NOT_FOUND("service not found"),
    SERVICE_NOT_IMPLEMENT_ANY_INTERFACE("the service does not implement any interfaces"),
    UNKNOWN_PROTOCOL("unknown protocol"),
    UNKNOWN_SERIALIZER("unknown serializer"),
    UNKNOWN_PACKAGE_TYPE("unknown package type"),
    SERIALIZER_NOT_FOUND("serializer not found");

    private final String message;

}