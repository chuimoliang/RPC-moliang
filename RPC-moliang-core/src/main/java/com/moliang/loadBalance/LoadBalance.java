package com.moliang.loadBalance;

import com.moliang.extension.SPI;

import java.util.List;

/**
 * @Use 负载均衡接口
 * @Author Chui moliang
 * @Date 2021/1/24 22:00
 * @Version 1.0
 */
@SPI
public interface LoadBalance {

    /**
     * 从现有服务地址列表中选择一个
     * @param serviceAddresses 服务地址列表
     * @param rpcServiceName 服务名称
     * @return 目标地址
     */
    String selectServiceAddress(List<String> serviceAddresses, String rpcServiceName);
}
