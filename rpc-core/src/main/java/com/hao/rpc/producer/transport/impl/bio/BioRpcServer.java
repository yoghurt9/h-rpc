package com.hao.rpc.producer.transport.impl.bio;

import com.hao.rpc.producer.transport.RpcServer;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

/**
 * 将服务注册到这个对象里面，当接收到请求的时候，这个对象会自动执行
 */
@Slf4j
public class BioRpcServer implements RpcServer {

    private ExecutorService threadPool;

    public BioRpcServer() {
        int CORE_POOL_SIZE = 5;
        int MAXIMUM_POOL_SIZE = 50;
        int KEEP_ALIVE_TIME = 60;
        int BLOCKING_QUEUE_CAPACITY = 100;

        this.threadPool = new ThreadPoolExecutor(
                CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE_TIME,
                TimeUnit.SECONDS, new ArrayBlockingQueue<>(BLOCKING_QUEUE_CAPACITY),
                Executors.defaultThreadFactory()
        );
    }

    @Override
    public void exec(int port) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            log.info("服务器启动……");
            Socket socket;
            while((socket = serverSocket.accept()) != null) {
                log.info("消费者连接: {}:{}", socket.getInetAddress(), socket.getPort());
                threadPool.execute(new MethodTask(socket));
            }
            threadPool.shutdown();
        } catch (IOException e) {
            log.error("服务器启动时有错误发生:", e);
        }
    }

}

