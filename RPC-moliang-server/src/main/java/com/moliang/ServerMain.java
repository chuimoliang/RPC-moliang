package com.moliang;

import com.moliang.annotation.EnableRpc;
import com.moliang.annotation.EnableRpcServer;
import com.moliang.transport.netty.server.NettyServer;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @Use
 * @Author Chui moliang
 * @Date 2021/1/24 23:47
 * @Version 1.0
 */
//@EnableRpcServer
@EnableRpc(packages = "com.moliang")
public class ServerMain {
    public static void main(String[] args) throws InterruptedException {
        // 通过注释注册服务
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(ServerMain.class);
        NettyServer ns = (NettyServer) ctx.getBean("nettyServer");
        ns.start();
    }
}
