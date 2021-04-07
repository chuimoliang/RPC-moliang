package com.moliang.test;

/**
 * @Use
 * @Author Chui moliang
 * @Date 2021/3/16 11:10
 * @Version 1.0
 */
public class Test {

    private static volatile int num = 1;

    public static void main(String[] args) {
        Thread t1 = new Thread(()->{
            int i = 0;
            while (true) {
                if (num == 1) {
                    System.out.println(++i + " : " + Thread.currentThread().getName());
                    num = 2;
                }
            }
        }, "A");
        Thread t2 = new Thread(()->{
            int i = 0;
            while (true) {
                if (num == 2) {
                    System.out.println(++i + " : " + Thread.currentThread().getName());
                    num = 3;
                }
            }
        }, "B");
        Thread t3 = new Thread(()->{
            int i = 0;
            while (true) {
                if (num == 3) {
                    System.out.println(++i + " : " + Thread.currentThread().getName());
                    num = 1;
                }
            }
        }, "C");
        t1.start();
        t2.start();
        t3.start();
    }

}
