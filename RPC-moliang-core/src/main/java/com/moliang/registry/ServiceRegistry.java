package com.moliang.registry;

import com.moliang.annotions.SPI;

import java.net.InetSocketAddress;

/**
 * @Use 注册服务接口
 * @Author Chui moliang
 * @Date 2021/1/22 20:40
 * @Version 1.0
 */
@SPI
public interface ServiceRegistry {
    void registryService(String serviceName, InetSocketAddress address);
}
