package com.moliang.transport.codec;

import com.moliang.convention.MyProtocol;
import com.moliang.convention.enums.SerializationType;
import com.moliang.entity.RpcMessage;
import com.moliang.serialize.KryoUtil;
import com.moliang.serialize.ProtostuffUtil;
import com.moliang.serialize.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;

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
            //写入魔数 四个字节
            buff.writeInt(MyProtocol.MAGIC_NUMBER);
            //版本号 一个字节
            buff.writeInt(MyProtocol.VERSION);
            //消息类型 一个字节
            buff.writeByte(mes.getMessageType());
            //序列化类型 一个字节
            buff.writeByte(mes.getCodec());
            byte[] bodyBytes = null;
            String codecName = SerializationType.getName(mes.getCodec());
            Serializer serializer = null;
            if(codecName.equals("kryo")) {
                serializer = new KryoUtil();
            } else if(codecName.equals("protostuff")) {
                serializer = new ProtostuffUtil();
            }
            bodyBytes = serializer.serialize(mes.getData());
            int fullLength = MyProtocol.HEAD_LENGTH;
            fullLength += bodyBytes.length;
            buff.writeInt(fullLength);
            // if messageType is not heartbeat message,fullLength = head length + body length
            buff.writeBytes(bodyBytes);
        } catch (Exception e) {
            log.error("Encode request error!", e);
        }
    }
}
