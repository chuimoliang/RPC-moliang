package com.moliang.convention;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @Use
 * @Author Chui moliang
 * @Date 2021/1/22 21:27
 * @Version 1.0
 */
public class MyProtocol {
    /**
     * 协议模型
     * |---魔数 (5个字节) ---|---版本 (1个字节) ---|
     * |---类型 (1个字节) ---|---消息数据长度 (int类型, 4个字节) ---|
     * |---消息数据---|
     *
     */
    public static final byte[] MAGIC_NUMBER = {(byte)'l',(byte)'i',(byte)'a',(byte)'n', (byte)'g'};
    public static final byte VERSION = 1;
    public static final byte REQUEST = 1;
    public static final byte RESPONSE = 2;
    //ping
    public static final byte REQUEST_HEART = 3;
    //pong
    public static final byte RESPONSE_HEART = 4;
    public static final int LENGTH = 8 * 1024 * 1024;
    public static final String PING = "ping";
    public static final String PONG = "pong";

}
