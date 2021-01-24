package com.moliang.registry.zk;

import com.moliang.registry.ServiceRegistry;
import com.moliang.registry.util.CuratorUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;

import java.net.InetSocketAddress;

/**
 * @Use
 * @Author Chui moliang
 * @Date 2021/1/24 20:34
 * @Version 1.0
 */
@Slf4j
public class ZkServiceRegistry implements ServiceRegistry {
    @Override
    public void registryService(String serviceName, InetSocketAddress address) {
        String servicePath = CuratorUtil.ZK_REGISTER_ROOT_PATH + "/" + serviceName + address.toString();
        CuratorFramework zkClient = CuratorUtil.getZkClient();
        CuratorUtil.createPersistentNode(zkClient, servicePath);
    }
}
