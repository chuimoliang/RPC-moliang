package com.moliang.registry.util;

import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @Use 线程池默认参数
 * @Author Chui moliang
 * @Date 2021/2/1 19:36
 * @Version 1.0
 */
@Setter
@Getter
public class CustomThreadPoolConfig {

    /** 核心池默认大小 **/
    private static final int DEFAULT_CORE_POOL_SIZE = 10;

    /** 线程默认最大数量 **/
    private static final int DEFAULT_MAXIMUM_POOL_SIZE_SIZE = 100;

    /** 额外线程的默认等待时间 **/
    private static final int DEFAULT_KEEP_ALIVE_TIME = 1;

    /** 默认时间单位 **/
    private static final TimeUnit DEFAULT_TIME_UNIT = TimeUnit.MINUTES;

    /** 等待队列默认大小 **/
    private static final int DEFAULT_BLOCKING_QUEUE_CAPACITY = 100;

    private static final int BLOCKING_QUEUE_CAPACITY = 100;
    private int corePoolSize = DEFAULT_CORE_POOL_SIZE;
    private int maximumPoolSize = DEFAULT_MAXIMUM_POOL_SIZE_SIZE;
    private long keepAliveTime = DEFAULT_KEEP_ALIVE_TIME;
    private TimeUnit unit = DEFAULT_TIME_UNIT;
    /** 有界队列 **/
    private BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(BLOCKING_QUEUE_CAPACITY);
}
