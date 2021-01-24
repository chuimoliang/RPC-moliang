package com.moliang.registry.zk;

import com.moliang.convention.exception.RpcErrorCode;
import com.moliang.convention.exception.RpcException;
import com.moliang.extension.ExtensionLoader;
import com.moliang.loadBalance.LoadBalance;
import com.moliang.registry.ServiceDiscovery;
import com.moliang.registry.util.CuratorUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * @Use
 * @Author Chui moliang
 * @Date 2021/1/24 20:33
 * @Version 1.0
 */
@Slf4j
public class ZkServiceDiscovery implements ServiceDiscovery {
    private final LoadBalance loadBalance;

    public ZkServiceDiscovery() {
        this.loadBalance = ExtensionLoader.getExtensionLoader(LoadBalance.class).getExtension("rand");
    }

    @Override
    public InetSocketAddress lookForService(String rpcServiceName) {
        CuratorFramework zkClient = CuratorUtil.getZkClient();
        List<String> serviceUrlList = CuratorUtil.getChildrenNodes(zkClient, rpcServiceName);
        if (serviceUrlList == null || serviceUrlList.size() == 0) {
            throw new RpcException(RpcErrorCode.SERVICE_CAN_NOT_BE_FOUND, rpcServiceName);
        }
        String targetServiceUrl = loadBalance.selectServiceAddress(serviceUrlList, rpcServiceName);
        log.info("成功找到服务地址:[{}]", targetServiceUrl);
        String[] socketAddressArray = targetServiceUrl.split(":");
        String host = socketAddressArray[0];
        int port = Integer.parseInt(socketAddressArray[1]);
        return new InetSocketAddress(host, port);
    }
}
