package com.moliang;

import com.moliang.extension.EnableRpc;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @Use
 * @Author Chui moliang
 * @Date 2021/1/25 19:12
 * @Version 1.0
 */
@EnableRpc(packages = "com.moliang")
public class ClientMain {
    public static void main(String[] args) throws InterruptedException {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(ClientMain.class);
        HelloController helloController = (HelloController) applicationContext.getBean("helloController");
        helloController.test();
    }
}
