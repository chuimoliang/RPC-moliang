package com.moliang.transport.netty.server;

import com.moliang.handler.RequestHandler;
import com.moliang.convention.MyProtocol;
import com.moliang.convention.enums.RpcResponseCode;
import com.moliang.convention.enums.SerializationType;
import com.moliang.entity.RpcMessage;
import com.moliang.entity.RpcRequest;
import com.moliang.entity.RpcResponse;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Use
 * @Author Chui moliang
 * @Date 2021/1/24 20:23
 * @Version 1.0
 */
@Slf4j
@Component
public class NettyServerHandler extends SimpleChannelInboundHandler {

    private final RequestHandler rpcRequestHandler;

    @Autowired
    public NettyServerHandler(RequestHandler rpcRequestHandler) {
        this.rpcRequestHandler = rpcRequestHandler;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof RpcMessage) {
            log.info("服务端收到消息: [{}] ", msg);
            byte messageType = ((RpcMessage) msg).getMessageType();
            RpcMessage rpcMessage = RpcMessage.builder()
                    .codecType(SerializationType.KRYO.getCode())
                    .build();
            if (messageType == MyProtocol.REQUEST_HEART) {
                rpcMessage.setMessageType(MyProtocol.RESPONSE_HEART);
                rpcMessage.setData(MyProtocol.PONG);
            } else {
                RpcRequest rpcRequest = (RpcRequest) ((RpcMessage) msg).getData();
                // Execute the target method (the method the client needs to execute) and return the method result
                Object result = rpcRequestHandler.handle(rpcRequest);
                log.info(String.format("服务器取得结果: %s", result.toString()));
                rpcMessage.setMessageType(MyProtocol.RESPONSE);
                if (ctx.channel().isActive() && ctx.channel().isWritable()) {
                    RpcResponse<Object> rpcResponse = RpcResponse.success(result, rpcRequest.getRequestId());
                    rpcMessage.setData(rpcResponse);
                } else {
                    RpcResponse<Object> rpcResponse = RpcResponse.fail(RpcResponseCode.FAIL);
                    rpcMessage.setData(rpcResponse);
                    log.error("现在无法写入响应，消息已删除");
                }
            }
            ctx.writeAndFlush(rpcMessage).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleState state = ((IdleStateEvent) evt).state();
            if (state == IdleState.READER_IDLE) {
                log.info("发生空闲检查，因此关闭连接");
                ctx.close();
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("服务器捕获异常");
        cause.printStackTrace();
        ctx.close();
    }
}
