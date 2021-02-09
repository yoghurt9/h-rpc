package com.hao.rpc.protocol;

import com.hao.rpc.entity.RpcResponse;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class RpcSessionHolder {

    private final static AtomicLong SESSION_ID = new AtomicLong(0);

    private static final Map<Long, CompletableFuture<RpcResponse>> SESSION_MAP = new ConcurrentHashMap<>();

    public static long getSessionId() {
        return SESSION_ID.getAndIncrement();
    }

    public static void addSession(long sessionId, CompletableFuture<RpcResponse> future) {
        SESSION_MAP.put(sessionId, future);
    }

    public static CompletableFuture<RpcResponse> removeSession(long sessionId) {
        return SESSION_MAP.remove(sessionId);
    }
}