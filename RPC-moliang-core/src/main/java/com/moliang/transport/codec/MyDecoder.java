package com.moliang.transport.codec;

import com.moliang.convention.MyProtocol;
import com.moliang.convention.enums.SerializationType;
import com.moliang.convention.exception.RpcErrorCode;
import com.moliang.convention.exception.RpcException;
import com.moliang.entity.RpcMessage;
import com.moliang.entity.RpcRequest;
import com.moliang.entity.RpcResponse;
import com.moliang.extension.ExtensionLoader;
import com.moliang.serialize.KryoUtil;
import com.moliang.serialize.ProtostuffUtil;
import com.moliang.serialize.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;

/**
 * @Use
 * @Author Chui moliang
 * @Date 2021/1/22 21:02
 * @Version 1.0
 */
@Slf4j
public class MyDecoder extends ReplayingDecoder<RpcMessage> {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        /**
         * 协议
         *
         * 魔数 + 版本号 + 消息类型 + 序列化类型 + 数据长度
         */
        int len = MyProtocol.MAGIC_NUMBER.length;
        byte[] tmp = new byte[len];
        in.readBytes(tmp);
        for (int i = 0; i < len; i++) {
            if (tmp[i] != MyProtocol.MAGIC_NUMBER[i]) {
                log.error("错误的魔数" + Arrays.toString(tmp));
                throw new RpcException(RpcErrorCode.MES_PROTO_ERROR);
            }
        }
        byte version = in.readByte();
        if (version != MyProtocol.VERSION) {
            log.error("无法识别的版本" + version);
            throw new RpcException(RpcErrorCode.MES_PROTO_ERROR);
        }
        byte messageType = in.readByte();
        byte codecType = in.readByte();
        RpcMessage mes = RpcMessage.builder()
                            .messageType(messageType)
                            .codecType(codecType).build();
        if (messageType == MyProtocol.REQUEST_HEART) {
            mes.setData(MyProtocol.PING);
            out.add(mes);
            return;
        }
        if (messageType == MyProtocol.RESPONSE_HEART) {
            mes.setData(MyProtocol.PONG);
            out.add(mes);
            return;
        }
        int dataLength = in.readInt();
        if (dataLength > 0) {
            byte[] bs = new byte[dataLength];
            in.readBytes(bs);
            // deserialize the object
            String codecName = SerializationType.getName(mes.getCodecType());
            log.info("codec name: [{}] ", codecName);
            Serializer serializer = ExtensionLoader.getExtensionLoader(Serializer.class)
                    .getExtension(codecName);
            if (messageType == MyProtocol.REQUEST) {
                RpcRequest tmpValue = serializer.deserialize(bs, RpcRequest.class);
                mes.setData(tmpValue);
            } else {
                RpcResponse tmpValue = serializer.deserialize(bs, RpcResponse.class);
                mes.setData(tmpValue);
            }
        }
        out.add(mes);
    }
    private void checkVersion(ByteBuf in) {
        // read the version and compare
        byte version = in.readByte();
        if (version != MyProtocol.VERSION) {
            log.error("无法识别的版本" + version);
            throw new RpcException(RpcErrorCode.MES_PROTO_ERROR);
        }
    }
}
