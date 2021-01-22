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
    public static final int MAGIC_NUMBER = 0x123456;
    public static final byte VERSION = 1;
    public static final byte TOTAL_LENGTH = 16;
    public static final byte REQUEST_TYPE = 1;
    public static final byte RESPONSE_TYPE = 2;
    //ping
    public static final byte HEARTBEAT_REQUEST_TYPE = 3;
    //pong
    public static final byte HEARTBEAT_RESPONSE_TYPE = 4;
    public static final int HEAD_LENGTH = 8;
    public static final String PING = "ping";
    public static final String PONG = "pong";
    public static final int MAX_FRAME_LENGTH = 8 * 1024 * 1024;

}
