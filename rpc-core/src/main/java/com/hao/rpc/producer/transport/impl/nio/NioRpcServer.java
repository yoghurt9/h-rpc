package com.hao.rpc.producer.transport.impl.nio;

import com.hao.rpc.codec.CommonDecoder;
import com.hao.rpc.codec.CommonEncoder;
import com.hao.rpc.producer.manager.ServiceManager;
import com.hao.rpc.producer.manager.impl.DefaultServiceManager;
import com.hao.rpc.producer.transport.RpcServer;
import com.hao.rpc.registry.ServiceRegistry;
import com.hao.rpc.serializer.CommonSerializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

@Slf4j
@Data
public class NioRpcServer implements RpcServer {

    private CommonSerializer serializer;
    private final InetSocketAddress localAddress;
    private final ServiceManager serviceManager;
    private final ServiceRegistry serviceRegistry;

    public NioRpcServer(String host, int port, ServiceRegistry serviceRegistry, CommonSerializer serializer) {
        localAddress = new InetSocketAddress(host, port);
        this.serviceRegistry = serviceRegistry;
        this.serviceManager = new DefaultServiceManager();
        this.serializer = serializer;
    }

    @Override
    public void exec() {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {

            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .option(ChannelOption.SO_BACKLOG, 256)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new CommonEncoder(serializer));
                            pipeline.addLast(new CommonDecoder());
                            pipeline.addLast(new NioServerHandler());
                        }
                    });
            ChannelFuture future = serverBootstrap.bind(localAddress).sync();
            future.channel().closeFuture().sync();

        } catch (InterruptedException e) {
            log.error("server startup failed: ", e);
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    /**
     * 将服务保存在本地的注册表，同时注册到Nacos上
     *
     * @param service 服务的实现类
     * @param serviceClass 服务接口的Class对象
     * @param <T> 服务接口类型
     */
    @Override
    public <T> void register(T service, Class<T> serviceClass) {
        String serviceName = serviceClass.getCanonicalName();
        serviceManager.addService(service, serviceName);
        serviceRegistry.publish(serviceName);
    }
}