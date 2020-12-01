package com.hao.rpc.entiry;

import lombok.Getter;

@Getter
public enum CodeMsg {
    SUCCESS(200, "success"),

    FAIL(500, "producer error"),

    BAD_REQUEST(400, "bad request"),
    NO_SUCH_SERVICE(404, "no such service"),
    NO_SUCH_METHOD(405, "no such method");

    Integer code;
    String msg;

    CodeMsg(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
