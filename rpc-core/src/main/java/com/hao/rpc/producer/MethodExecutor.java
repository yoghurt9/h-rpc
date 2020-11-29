package com.hao.rpc.producer;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

/**
 * 将服务注册到这个对象里面，当接收到请求的时候，这个对象会自动执行
 */
@Slf4j
public class MethodExecutor {
    private ExecutorService threadPool;

    public MethodExecutor() {
        this.threadPool = new ThreadPoolExecutor(
                5, 50, 60,
                TimeUnit.SECONDS, new ArrayBlockingQueue<>(100),
                Executors.defaultThreadFactory()
        );
    }

    public void register(Object service, int port) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            log.info("服务器正在启动...");
            Socket socket;
            while((socket = serverSocket.accept()) != null) {
                log.info("客户端连接！Ip为：" + socket.getInetAddress());
                threadPool.execute(new MethodTask(socket, service));
            }
        } catch (IOException e) {
            log.error("连接时有错误发生：", e);
        }
    }
}
