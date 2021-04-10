package com.moliang.context;

import org.springframework.util.StringUtils;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

/**
 * @Use
 * @Author Chui moliang
 * @Date 2021/4/9 20:04
 * @Version 1.0
 */
public class RpcContext {

    /**
     * 线程本地上下文. (API, ThreadLocal, ThreadSafe)
     * <p>
     * 注意: RpcContext是一个临时状态持有者。 每次发送或接收请求时，RpcContext中的状态都会更改。
     *
     */

    private static final ThreadLocal<RpcContext> LOCAL = new ThreadLocal<RpcContext>() {
        @Override
        protected RpcContext initialValue() {
            return new RpcContext();
        }
    };

    public static RpcContext getContext() {
        return LOCAL.get();
    }

    private Object request;
    private Object response;

    private boolean remove = true;


    protected RpcContext() {
    }

    public boolean canRemove() {
        return remove;
    }

    public void clearAfterEachInvoke(boolean remove) {
        this.remove = remove;
    }

    public static void restoreContext(RpcContext oldContext) {
        LOCAL.set(oldContext);
    }

    public static void removeContext() {
        if (LOCAL.get().canRemove()) {
            LOCAL.remove();
        }
    }

    public Object getRequest() {
        return request;
    }

    public void setRequest(Object request) {
        this.request = request;
    }


    /**
     * Get the response object of the underlying RPC protocol, e.g. HttpServletResponse
     *
     * @return null if the underlying protocol doesn't provide support for getting response
     */
    public Object getResponse() {
        return response;
    }

    public void setResponse(Object response) {
        this.response = response;
    }

    /**
     * get CompletableFuture.
     *
     * @param <T>
     * @return future
     */
    public <T> CompletableFuture<T> getCompletableFuture() {
        return FutureContext.getContext().getCompletableFuture();
    }

    /**
     * get future.
     *
     */
    public <T> Future<T> getFuture() {
        return FutureContext.getContext().getCompletableFuture();
    }

    /**
     * set future.
     */
    public void setFuture(CompletableFuture<?> future) {
        FutureContext.getContext().setFuture(future);
    }

    public static void setRpcContext() {
        RpcContext rpcContext = RpcContext.getContext();
        //rpcContext.setConsumerUrl(url);
    }

}