package com.moliang.entity;


import lombok.*;

/**
 * @Use
 * @Author Chui moliang
 * @Date 2021/1/21 19:59
 * @Version 1.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class RpcMessage {

    //消息类型
    private byte messageType;
    //序列化类型
    private byte codec;
    //请求id
    private int requestId;
    //请求数据
    private Object data;

}
