package com.moliang.registry;

import com.moliang.annotions.SPI;

import java.net.InetSocketAddress;

/**
 * @Use 寻找服务接口
 * @Author Chui moliang
 * @Date 2021/1/22 20:38
 * @Version 1.0
 */
@SPI
public interface ServiceDiscovery {
    InetSocketAddress lookForService(String serviceName);
}
