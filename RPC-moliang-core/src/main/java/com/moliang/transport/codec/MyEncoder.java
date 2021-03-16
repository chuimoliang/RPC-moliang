package com.moliang.transport.codec;

import com.moliang.convention.MyProtocol;
import com.moliang.convention.enums.SerializationType;
import com.moliang.entity.RpcMessage;
import com.moliang.entity.RpcRequest;
import com.moliang.extension.ExtensionLoader;
import com.moliang.serialize.KryoUtil;
import com.moliang.serialize.ProtostuffUtil;
import com.moliang.serialize.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

/**
 * @Use
 * @Author Chui moliang
 * @Date 2021/1/22 21:02
 * @Version 1.0
 */
@Slf4j
public class MyEncoder extends MessageToByteEncoder<RpcMessage> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, RpcMessage mes, ByteBuf buff) throws Exception {
        try {
            log.info("Start encoding...");
            /**
             * 协议
             *
             * 魔数 + 版本号 + 消息类型 + 序列化类型 + 数据长度
             */
            //写入魔数 5个字节
            buff.writeBytes(MyProtocol.MAGIC_NUMBER);
            //版本号 一个字节
            buff.writeByte(MyProtocol.VERSION);
            //消息类型 一个字节
            buff.writeByte(mes.getMessageType());
            //序列化类型 一个字节
            buff.writeByte(mes.getCodecType());
            byte[] bodyBytes = null;
            String codecName = SerializationType.getName(mes.getCodecType());
            Serializer serializer = ExtensionLoader.getExtensionLoader(Serializer.class)
                    .getExtension(codecName);
            bodyBytes = serializer.serialize(mes.getData());
            int dataLength = bodyBytes.length;
            // 数据长度
            buff.writeInt(dataLength);
            // 数据
            buff.writeBytes(bodyBytes);
            log.info("编码完成...");
        } catch (Exception e) {
            log.error("编码请求错误!", e);
        }
    }

    public static void main(String[] args) {
        MyEncoder encoder = new MyEncoder();
        MyDecoder decoder = new MyDecoder();
        RpcMessage mes = RpcMessage.builder().data(RpcRequest.builder().group("fa").build())
                .codecType(SerializationType.KRYO.getCode())
                .messageType(MyProtocol.REQUEST)
                .build();
    }
}
