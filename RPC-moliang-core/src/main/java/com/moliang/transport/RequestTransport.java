package com.moliang.transport;

import com.moliang.entity.RpcRequest;
import com.moliang.extension.SPI;

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
