package com.moliang.utils;

import com.moliang.transport.netty.server.NettyServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @Use
 * @Author Chui moliang
 * @Date 2021/3/16 15:12
 * @Version 1.0
 */
@Slf4j
public class EnableServerUtil implements ApplicationContextAware {

    private ApplicationContext ctx;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.ctx = applicationContext;
        NettyServer ns = ctx.getBean(NettyServer.class);
        new Thread(ns::start).start();
        log.info("Netty服务端启动------------");
    }
}
