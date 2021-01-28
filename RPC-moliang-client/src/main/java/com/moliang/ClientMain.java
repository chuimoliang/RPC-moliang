package com.moliang;

import com.moliang.convention.RpcServiceProperties;
import com.moliang.transport.RequestTransport;
import com.moliang.transport.netty.server.NettyServer;
import com.moliang.transport.netty.server.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @Use
 * @Author Chui moliang
 * @Date 2021/1/25 19:12
 * @Version 1.0
 */
public class ClientMain {
    public static void main(String[] args) {
        ApplicationContext applicationContext = new Test().getUse();
        NettyServer nettyServer = (NettyServer) applicationContext.getBean("nettyServer");
        HelloController helloController = (HelloController) applicationContext.getBean("helloController");
        //helloController.test();
    }
}
