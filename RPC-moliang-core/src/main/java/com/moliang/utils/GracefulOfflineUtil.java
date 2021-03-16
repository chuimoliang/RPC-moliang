package com.moliang.utils;

import com.moliang.registry.util.CuratorUtil;
import com.moliang.registry.util.ThreadPoolFactoryUtils;
import com.moliang.transport.netty.server.NettyServer;
import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

/**
 * @Use 实现优雅下线
 * @Author Chui moliang
 * @Date 2021/3/16 15:38
 * @Version 1.0
 */
@Slf4j
public class GracefulOfflineUtil {

    private static final GracefulOfflineUtil gracefulOfflineUtil = new GracefulOfflineUtil();

    public static GracefulOfflineUtil getGracefulOfflineUtil() {
        return gracefulOfflineUtil;
    }

    public void offline() {
        log.info("增加服务下线钩子方法");
        Runtime.getRuntime().addShutdownHook(new Thread(()->{
            try {
                InetSocketAddress inetSocketAddress = new InetSocketAddress(InetAddress.getLocalHost().getHostAddress(), NettyServer.PORT);
                CuratorUtil.clearRegistry(CuratorUtil.getZkClient(), inetSocketAddress);
            } catch (UnknownHostException ignored) {
            }
            ThreadPoolFactoryUtils.shutDownAllThreadPool();
        }));
    }

}
