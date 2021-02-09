package com.hao.rpc.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PackageType {

    /**
     * 请求体
     */
    REQUEST_PACK((byte) 0),

    /**
     * 响应体
     */
    RESPONSE_PACK((byte) 1);

    private final byte code;
}
