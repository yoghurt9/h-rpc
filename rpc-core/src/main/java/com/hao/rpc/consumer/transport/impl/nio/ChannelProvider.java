package com.hao.rpc.consumer.transport.impl.nio;

import com.hao.rpc.codec.CommonDecoder;
import com.hao.rpc.codec.CommonEncoder;
import com.hao.rpc.enumeration.RpcError;
import com.hao.rpc.exception.RpcException;
import com.hao.rpc.serializer.CommonSerializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.Date;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * 用于获取 Channel 对象
 */
@Slf4j
public class ChannelProvider {

    private static Bootstrap bootstrap = initializeBootstrap();

    private static final int MAX_RETRY_COUNT = 5;
    private static Channel channel;

    public static Channel get(InetSocketAddress inetSocketAddress, CommonSerializer serializer) {
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) {
                /*自定义序列化编解码器*/
                // RpcResponse -> ByteBuf
                ch.pipeline().addLast(new CommonEncoder(serializer))
                        .addLast(new CommonDecoder())
                        .addLast(new NioClientHandler());
            }
        });
        Semaphore semaphore = new Semaphore(0);
        try {
            connect(bootstrap, inetSocketAddress, semaphore);
            semaphore.acquire();
        } catch (InterruptedException e) {
            log.error("获取channel时有错误发生:", e);
        }
        return channel;
    }

    private static void connect(Bootstrap bootstrap, InetSocketAddress inetSocketAddress, Semaphore semaphore) {
        connect(bootstrap, inetSocketAddress, MAX_RETRY_COUNT, semaphore);
    }

    private static void connect(Bootstrap bootstrap, InetSocketAddress inetSocketAddress, int retry, Semaphore semaphore) {
        bootstrap.connect(inetSocketAddress).addListener((ChannelFutureListener) future -> {
            if (future.isSuccess()) {
                log.info("客户端连接成功!");
                channel = future.channel();
                semaphore.release();
                return;
            }
            if (retry == 0) {
                log.error("客户端连接失败:重试次数已用完，放弃连接！");
                semaphore.release();
                throw new RpcException(RpcError.CLIENT_CONNECT_SERVER_FAILURE);
            }
            // 第几次重连
            int order = (MAX_RETRY_COUNT - retry) + 1;
            // 本次重连的间隔: 2 4 6 8 10
            int delay = order * 2;
            log.error("{}: 连接失败，第 {} 次重连 . . .", new Date(), order);
            bootstrap.config().group().schedule(() -> connect(bootstrap, inetSocketAddress, retry - 1, semaphore), delay, TimeUnit
                    .SECONDS);
        });
    }

    private static Bootstrap initializeBootstrap() {
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                //连接的超时时间，超过这个时间还是建立不上的话则代表连接失败
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                //是否开启 TCP 底层心跳机制
                .option(ChannelOption.SO_KEEPALIVE, true)
                //TCP默认开启了 Nagle 算法，该算法的作用是尽可能的发送大数据快，减少网络传输。TCP_NODELAY 参数的作用就是控制是否启用 Nagle 算法。
                .option(ChannelOption.TCP_NODELAY, true);
        return bootstrap;
    }

}
