package com.moliang.transport.netty.server;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @Use
 * @Author Chui moliang
 * @Date 2021/1/25 17:07
 * @Version 1.0
 */
public class Test {
    public ApplicationContext getUse() {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        return ctx;
    }

    public static void main(String[] args) {
        Test t= new Test();
        t.getUse();
    }
}
