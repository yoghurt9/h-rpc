package com.hao.rpc.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * RPC调用过程中的错误
 */
@AllArgsConstructor
@Getter
public enum RpcError {

    UNKNOWN_ERROR("unknown error"),

    // ======= 服务端自动扫描、注册 ========
    SERVICE_SCAN_PACKAGE_NOT_FOUND("service scan package not found"),

    // ========== 服务注册与发现 ========
    CONNECT_TO_SERVICE_REGISTRY_FAIL("connect to service registry fail"),
    REGISTER_SERVICE_FAIL("register service fail"),

    // =========== 网络 ============
    CLIENT_CONNECT_SERVER_FAIL("client connect server fail"),

    // ============服务=============
    SERVICE_INVOCATION_FAIL("service invocation fail"),
    SERVICE_NOT_FOUND("service not found"),
    SERVICE_NOT_IMPLEMENT_ANY_INTERFACE("the service does not implement any interfaces"),

    // ============= 协议 ================
    UNKNOWN_PROTOCOL("unknown protocol"),
    UNKNOWN_VERSION("unknown version"),

    // ========传输数据类型，比如request, response...=========
    UNKNOWN_PACKAGE_TYPE("unknown package type"),

    // ======= 序列化器 ======
    UNKNOWN_SERIALIZER("unknown serializer"),
    SERIALIZER_NOT_FOUND("serializer not found");

    private final String message;

}