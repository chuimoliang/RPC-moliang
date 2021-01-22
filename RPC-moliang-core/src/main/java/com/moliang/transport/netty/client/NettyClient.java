package com.moliang.transport.netty.client;

import com.moliang.entity.RpcRequest;
import com.moliang.transport.RequestTransport;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @Use
 * @Author Chui moliang
 * @Date 2021/1/22 20:23
 * @Version 1.0
 */
@Slf4j
public class NettyClient implements RequestTransport {
    private final NioEventLoopGroup eventLoopGroup;
    private final Bootstrap bootstrap;

    public NettyClient() {
        //默认开启线程数量是CPU核心数*2
        this.eventLoopGroup = new NioEventLoopGroup();
        this.bootstrap = new Bootstrap();
        this.bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.INFO))
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        ChannelPipeline p = ch.pipeline();
                        // If no data is sent to the server within 15 seconds, a heartbeat request is sent
                        p.addLast(new IdleStateHandler(0, 5, 0, TimeUnit.SECONDS));
                        //p.addLast(new RpcMessageEncoder());
                        //p.addLast(new RpcMessageDecoder());
                        //p.addLast(new NettyRpcClientHandler());
                    }
                });
        //this.serviceDiscovery = ExtensionLoader.getExtensionLoader(ServiceDiscovery.class).getExtension("zk");
        //this.unprocessedRequests = SingletonFactory.getInstance(UnprocessedRequests.class);
        //this.channelProvider = SingletonFactory.getInstance(ChannelProvider.class);
    }

    @Override
    public Object send(RpcRequest rpcRequest) {
        return null;
    }
}
