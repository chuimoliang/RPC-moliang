package com.moliang.transport.netty.client;

import com.moliang.convention.MyProtocol;
import com.moliang.convention.enums.SerializationType;
import com.moliang.entity.RpcMessage;
import com.moliang.entity.RpcRequest;
import com.moliang.entity.RpcResponse;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;

/**
 * @Use
 * @Author Chui moliang
 * @Date 2021/1/22 21:07
 * @Version 1.0
 */
@Slf4j
@Component
public class NettyClientHandler extends SimpleChannelInboundHandler<RpcMessage> {

    private final NettyClient nettyClient;
    private final UnprocessedRequest unprocessedRequests;
    @Autowired
    public NettyClientHandler(NettyClient nettyClient, UnprocessedRequest unprocessedRequests) {
        super();
        this.nettyClient = nettyClient;
        this.unprocessedRequests = unprocessedRequests;
    }

    /**
     * 在父类SimpleChannelInboundHandler中已经调用了ReferenceCountUtil.release()方法
     * 不需要重复释放缓冲区空间
     * @param channelHandlerContext
     * @param msg
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcMessage msg) throws Exception {
        log.info("client receive msg: [{}]", msg);
        byte messageType = msg.getMessageType();
        if (messageType == MyProtocol.RESPONSE_HEART) {
            log.info("heart [{}]", msg.getData());
        } else if (messageType == MyProtocol.RESPONSE) {
            RpcResponse<Object> rpcResponse = (RpcResponse<Object>) msg.getData();
            unprocessedRequests.complete(rpcResponse);
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleState state = ((IdleStateEvent) evt).state();
            if (state == IdleState.WRITER_IDLE) {
                log.info("write idle happen [{}]", ctx.channel().remoteAddress());
                Channel channel = nettyClient.getChannel((InetSocketAddress) ctx.channel().remoteAddress());
                RpcMessage rpcMessage = RpcMessage.builder()
                        .messageType(MyProtocol.REQUEST_HEART)
                        .codecType(SerializationType.KRYO.getCode())
                        .data(MyProtocol.PING)
                        .build();
                channel.writeAndFlush(rpcMessage).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("客户端异常：", cause);
        cause.printStackTrace();
        ctx.close();
    }
}
