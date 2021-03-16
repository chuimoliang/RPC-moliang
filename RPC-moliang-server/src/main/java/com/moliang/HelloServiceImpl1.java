package com.moliang;

import com.moliang.annotation.RpcService;
import com.moliang.api.Hello;
import com.moliang.api.HelloService;
import lombok.extern.slf4j.Slf4j;

/**
 * @Use
 * @Author Chui moliang
 * @Date 2021/3/16 16:33
 * @Version 1.0
 */
@Slf4j
@RpcService(version = "1.1", group = "h1")
public class HelloServiceImpl1 implements HelloService {

    static {
        System.out.println("HelloServiceImpl1被创建");
    }

    @Override
    public String hello(Hello hello) {
        log.info("HelloServiceImpl1收到: {}.", hello.getMessage());
        String result = "Hello description is " + hello.getDescription();
        log.info("HelloServiceImpl1返回: {}.", result);
        return result;
    }
}
