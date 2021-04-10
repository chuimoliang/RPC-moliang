package com.moliang.context;

import org.springframework.util.concurrent.FutureAdapter;

import java.util.concurrent.CompletableFuture;

/**
 * @Use 异步调用结果上下文
 * @Author Chui moliang
 * @Date 2021/4/10 9:14
 * @Version 1.0
 */

public class FutureContext {

    /**
     * 用于异步调用方案。
     * 但是，如果要调用的方法结果为 CompletableFuture 类型, 则无需使用此类，因为将直接获得 Future 响应。
     * 在使用同一线程进行另一个调用之前，请记住保存“将来”引用, 否则,
     * 当前的 Future将被新的 Future覆盖，这意味着将失去获得返回值的机会.
     */

    private static ThreadLocal<FutureContext> futureTL = new ThreadLocal<FutureContext>() {
        @Override
        protected FutureContext initialValue() {
            return new FutureContext();
        }
    };

    public static FutureContext getContext() {
        return futureTL.get();
    }

    private CompletableFuture<?> future;

    /**
     * get future.
     *
     * @param <T>
     * @return future
     */
    @SuppressWarnings("unchecked")
    public <T> CompletableFuture<T> getCompletableFuture() {
        return (CompletableFuture<T>) future;
    }

    /**
     * set future.
     *
     * @param future
     */
    public void setFuture(CompletableFuture<?> future) {
        this.future = future;
    }

}
