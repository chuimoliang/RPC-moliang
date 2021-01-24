package com.moliang.transport.netty.client;

import com.moliang.convention.MyProtocol;
import com.moliang.convention.enums.SerializationType;
import com.moliang.entity.RpcMessage;
import com.moliang.entity.RpcRequest;
import com.moliang.entity.RpcResponse;
import com.moliang.extension.ExtensionLoader;
import com.moliang.registry.ServiceDiscovery;
import com.moliang.transport.RequestTransport;
import com.moliang.transport.codec.MyDecoder;
import com.moliang.transport.codec.MyEncoder;
import com.moliang.transport.netty.ChannelProvider;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @Use
 * @Author Chui moliang
 * @Date 2021/1/22 20:23
 * @Version 1.0
 */
@Slf4j
@Component
public class NettyClient implements RequestTransport {
    private final NioEventLoopGroup eventLoopGroup;
    private final Bootstrap bootstrap;
    private final ServiceDiscovery serviceDiscovery;

    private final UnprocessedRequest unprocessedRequests;
    private final ChannelProvider channelProvider;

    @Autowired
    private NettyClientHandler nettyClientHandler;

    @Autowired
    public NettyClient(UnprocessedRequest unprocessedRequests, ChannelProvider channelProvider) {
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
                        // 没有数据发送的情况下,每十五秒发送一次心跳
                        p.addLast(new IdleStateHandler(0, 5, 0, TimeUnit.SECONDS));
                        p.addLast(new MyEncoder());
                        p.addLast(new MyDecoder());
                        p.addLast(nettyClientHandler);
                    }
                });
        this.serviceDiscovery = ExtensionLoader.getExtensionLoader(ServiceDiscovery.class).getExtension("zk");
        this.unprocessedRequests = unprocessedRequests;
        this.channelProvider = channelProvider;
    }

    public Channel doConnect(InetSocketAddress inetSocketAddress) throws ExecutionException, InterruptedException {
        CompletableFuture<Channel> completableFuture = new CompletableFuture<>();
        bootstrap.connect(inetSocketAddress).addListener((ChannelFutureListener) future -> {
            if (future.isSuccess()) {
                log.info("客户端与 [{}] 连接成功!", inetSocketAddress.toString());
                completableFuture.complete(future.channel());
            } else {
                throw new IllegalStateException();
            }
        });
        return completableFuture.get();
    }

    @Override
    public Object send(RpcRequest rpcRequest) {
        CompletableFuture<RpcResponse<Object>> resultFuture = new CompletableFuture<>();
        String rpcServiceName = rpcRequest.toRpcProperties().toRpcServiceName();
        InetSocketAddress inetSocketAddress = serviceDiscovery.lookForService(rpcServiceName);
        Channel channel = null;
        try {
            channel = getChannel(inetSocketAddress);
        } catch (Exception e) {
            log.error("客户端消息发送失败 [{}]" , e.getMessage());
            return null;
        }
        if (channel.isActive()) {
            unprocessedRequests.put(rpcRequest.getRequestId(), resultFuture);
            RpcMessage rpcMessage = RpcMessage.builder()
                    .messageType(MyProtocol.REQUEST)
                    .codecType(SerializationType.KRYO.getCode())
                    .data(rpcRequest).build();
            channel.writeAndFlush(rpcMessage).addListener((ChannelFutureListener) future -> {
                if (future.isSuccess()) {
                    log.info("客户端成功发送消息: [{}]", rpcMessage);
                } else {
                    future.channel().close();
                    resultFuture.completeExceptionally(future.cause());
                    log.error("发送失败: ", future.cause());
                }
            });
        } else {
            throw new IllegalStateException();
        }

        return resultFuture;
    }

    public Channel getChannel(InetSocketAddress inetSocketAddress) throws ExecutionException, InterruptedException {
        Channel channel = channelProvider.get(inetSocketAddress);
        if (channel == null) {
            channel = doConnect(inetSocketAddress);
            channelProvider.set(inetSocketAddress, channel);
        }
        return channel;
    }

    public void close() {
        eventLoopGroup.shutdownGracefully();
    }
}
