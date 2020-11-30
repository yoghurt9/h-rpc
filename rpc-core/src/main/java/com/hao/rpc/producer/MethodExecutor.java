package com.hao.rpc.producer;

import com.hao.rpc.registry.ServiceManager;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

/**
 * 将服务注册到这个对象里面，当接收到请求的时候，这个对象会自动执行
 */

/**
 *     private static final int CORE_POOL_SIZE = 5;
 *     private static final int MAXIMUM_POOL_SIZE = 50;
 *     private static final int KEEP_ALIVE_TIME = 60;
 *     private static final int BLOCKING_QUEUE_CAPACITY = 100;
 *     private final ExecutorService threadPool;
 *     private RequestHandler requestHandler = new RequestHandler();
 *     private final ServiceRegistry serviceRegistry;
 *
 *     public RpcServer(ServiceRegistry serviceRegistry) {
 *         this.serviceRegistry = serviceRegistry;
 *         BlockingQueue<Runnable> workingQueue = new ArrayBlockingQueue<>(BLOCKING_QUEUE_CAPACITY);
 *         ThreadFactory threadFactory = Executors.defaultThreadFactory();
 *         threadPool = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE_TIME, TimeUnit.SECONDS, workingQueue, threadFactory);
 *     }
 */
@Slf4j
public class MethodExecutor {

    private ExecutorService threadPool;
    private ServiceManager serviceManager;

    public MethodExecutor(ServiceManager serviceManager) {
        int CORE_POOL_SIZE = 5;
        int MAXIMUM_POOL_SIZE = 50;
        int KEEP_ALIVE_TIME = 60;
        int BLOCKING_QUEUE_CAPACITY = 100;

        this.threadPool = new ThreadPoolExecutor(
                CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE_TIME,
                TimeUnit.SECONDS, new ArrayBlockingQueue<>(BLOCKING_QUEUE_CAPACITY),
                Executors.defaultThreadFactory()
        );
        this.serviceManager = serviceManager;
    }

    public void exec(int port) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            log.info("服务器启动……");
            Socket socket;
            while((socket = serverSocket.accept()) != null) {
                log.info("消费者连接: {}:{}", socket.getInetAddress(), socket.getPort());
                threadPool.execute(new MethodTask(socket, serviceManager));
            }
            threadPool.shutdown();
        } catch (IOException e) {
            log.error("服务器启动时有错误发生:", e);
        }
    }

}
