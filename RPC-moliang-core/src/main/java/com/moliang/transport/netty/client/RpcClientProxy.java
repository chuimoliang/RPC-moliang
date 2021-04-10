package com.moliang.transport.netty.client;

import com.moliang.context.RpcContext;
import com.moliang.convention.RpcServiceProperties;
import com.moliang.convention.enums.RpcResponseCode;
import com.moliang.convention.exception.RpcErrorCode;
import com.moliang.convention.exception.RpcException;
import com.moliang.entity.RpcRequest;
import com.moliang.entity.RpcResponse;
import com.moliang.transport.RequestTransport;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * 动态代理类。
 * 当动态代理对象调用方法时，它实际上会调用以下invoke方法。
 * 正是由于动态代理，客户端调用的远程方法就像调用本地方法一样（中间过程被屏蔽）
 *
 */
@Slf4j
public class RpcClientProxy implements InvocationHandler {

    private static final String INTERFACE_NAME = "interfaceName";
    private final RequestTransport rpcRequestTransport;
    private final RpcServiceProperties rpcServiceProperties;

    public RpcClientProxy(RequestTransport rpcRequestTransport, RpcServiceProperties rpcServiceProperties) {
        this.rpcRequestTransport = rpcRequestTransport;
        // 组装服务参数
        if (rpcServiceProperties.getGroup() == null) {
            rpcServiceProperties.setGroup("");
        }
        if (rpcServiceProperties.getVersion() == null) {
            rpcServiceProperties.setVersion("");
        }
        this.rpcServiceProperties = rpcServiceProperties;
    }


    public RpcClientProxy(RequestTransport rpcRequestTransport) {
        this.rpcRequestTransport = rpcRequestTransport;
        this.rpcServiceProperties = RpcServiceProperties.builder().group("").version("").build();
    }

    /**
     * 获取代理对象
     */
    @SuppressWarnings("unchecked")
    public <T> T getProxy(Class<T> clazz) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[]{clazz}, this);
    }

    /**
     * 当使用代理对象调用方法时，实际上会调用此方法。
     * 代理对象是您通过getProxy方法获得的对象。
     */
    @SneakyThrows
    @SuppressWarnings("unchecked")
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        log.info("调用方法: [{}]", method.getName());
        RpcRequest rpcRequest = RpcRequest.builder().methodName(method.getName())
                .parameters(args)
                .interfaceName(method.getDeclaringClass().getName())
                .paramTypes(method.getParameterTypes())
                .requestId(UUID.randomUUID().toString())
                .group(rpcServiceProperties.getGroup())
                .version(rpcServiceProperties.getVersion())
                .async(rpcServiceProperties.isAsyncMode())
                .build();
        RpcResponse<Object> rpcResponse;
        CompletableFuture<RpcResponse<Object>> completableFuture =
                (CompletableFuture<RpcResponse<Object>>) rpcRequestTransport.send(rpcRequest);
        if (rpcServiceProperties.isAsyncMode()) {
            RpcContext.getContext().setFuture(completableFuture);
            return null;
        }
        rpcResponse = completableFuture.get();
        this.check(rpcResponse, rpcRequest);
        return rpcResponse.getData();
    }

    private void check(RpcResponse<Object> rpcResponse, RpcRequest rpcRequest) {
        if (rpcResponse == null) {
            throw new RpcException(RpcErrorCode.SERVICE_INVOCATION_FAILURE, INTERFACE_NAME + ":" + rpcRequest.getInterfaceName());
        }

        if (!rpcRequest.getRequestId().equals(rpcResponse.getRequestId())) {
            throw new RpcException(RpcErrorCode.REQUEST_NOT_MATCH_RESPONSE, INTERFACE_NAME + ":" + rpcRequest.getInterfaceName());
        }

        if (rpcResponse.getCode() == null || !rpcResponse.getCode().equals(RpcResponseCode.SUCCESS.getCode())) {
            throw new RpcException(RpcErrorCode.SERVICE_INVOCATION_FAILURE, INTERFACE_NAME + ":" + rpcRequest.getInterfaceName());
        }
    }
}
