package com.moliang.test;

/**
 * @Use
 * @Author Chui moliang
 * @Date 2021/3/16 11:10
 * @Version 1.0
 */
public class Test {
    static {
        System.out.println(System.currentTimeMillis() + "--------------Test 静态代码块 ---------------");
    }

    public Test() {
        System.out.println(System.currentTimeMillis() + "--------------Test 构造函数 ---------------");
    }
}
