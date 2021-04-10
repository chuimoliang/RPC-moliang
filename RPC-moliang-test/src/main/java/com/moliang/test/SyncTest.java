package com.moliang.test;

import io.netty.util.concurrent.SingleThreadEventExecutor;
import lombok.SneakyThrows;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @Use
 * @Author Chui moliang
 * @Date 2021/4/7 16:51
 * @Version 1.0
 */
public class SyncTest {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService service = Executors.newSingleThreadExecutor();
        Future<Integer> future = service.submit(()->{
            Thread.sleep(2000);
            return 1;
        });
        System.out.println("----------");
        System.out.println(future.isDone());
        System.out.println(future.get());
        System.out.println(future.get());
        System.out.println(future.isDone());
        System.out.println(future.isCancelled());
        future.cancel(false);
        System.out.println(future.isCancelled());
        System.out.println("----------");
        service.shutdown();
    }
}
