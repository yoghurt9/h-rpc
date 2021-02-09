package com.hao.rpc.producer.transport.impl.nio;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class RpcRequestProcessor {
    private static final ThreadPoolExecutor THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(10, 10, 60L, TimeUnit.SECONDS, new ArrayBlockingQueue<>(10000));

    public static void submitRequest(Runnable task) {
        THREAD_POOL_EXECUTOR.submit(task);
    }
}