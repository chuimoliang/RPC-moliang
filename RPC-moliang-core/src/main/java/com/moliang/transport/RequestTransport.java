package com.moliang.transport;

import com.moliang.annotions.SPI;
import com.moliang.entity.RpcRequest;

/**
 * @Use
 * @Author Chui moliang
 * @Date 2021/1/22 20:27
 * @Version 1.0
 */
@SPI
public interface RequestTransport {
    Object send(RpcRequest rpcRequest);
}
