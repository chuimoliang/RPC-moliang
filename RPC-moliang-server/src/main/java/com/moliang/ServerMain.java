package com.moliang;

import com.moliang.convention.RpcServiceProperties;
import com.moliang.transport.netty.server.NettyServer;
import com.moliang.transport.netty.server.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @Use
 * @Author Chui moliang
 * @Date 2021/1/24 23:47
 * @Version 1.0
 */
public class ServerMain {
    public static void main(String[] args) throws InterruptedException {
        // 通过注释注册服务
        //AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(ServerMain.class);
        ApplicationContext applicationContext = new Test().getUse();
        NettyServer nettyServer = (NettyServer) applicationContext.getBean("nettyServer");
        // 手动注册服务
        //nettyServer = new NettyServer();
        HelloService helloService2 = new HelloServiceImpl2();
        RpcServiceProperties rpcServiceProperties = RpcServiceProperties.builder()
                .group("test2").version("version2").build();
        nettyServer.registerService(helloService2, rpcServiceProperties);
        nettyServer.start();
    }
}
