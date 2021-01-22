package com.moliang.transport.netty.client;

import com.moliang.entity.RpcRequest;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @Use
 * @Author Chui moliang
 * @Date 2021/1/22 21:07
 * @Version 1.0
 */
@Slf4j
public class NettyClientHandler extends SimpleChannelInboundHandler<RpcRequest> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequest rpcRequest) throws Exception {

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        super.userEventTriggered(ctx, evt);
    }
}
