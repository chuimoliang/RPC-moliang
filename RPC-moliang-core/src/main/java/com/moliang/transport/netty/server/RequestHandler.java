package com.moliang.transport.netty.server;

import com.moliang.convention.exception.RpcException;
import com.moliang.entity.RpcRequest;
import com.moliang.factory.SingletonFactory;
import com.moliang.provider.ServiceProvider;
import com.moliang.provider.ServiceProviderImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @Use 处理客户端请求
 * @Author Chui moliang
 * @Date 2021/1/24 3:07
 * @Version 1.0
 */
@Slf4j
public class RequestHandler {

    private final ServiceProvider serviceProvider;

    public RequestHandler() {
        this.serviceProvider = SingletonFactory.getInstance(ServiceProviderImpl.class);
    }

    /**
     * 处理rpcRequest：调用相应的方法，然后返回该方法
     */
    public Object handle(RpcRequest rpcRequest) {
        Object service = serviceProvider.getService(rpcRequest.toRpcProperties());
        return invokeTargetMethod(rpcRequest, service);
    }

    /**
     * 获取方法执行结果
     */
    private Object invokeTargetMethod(RpcRequest rpcRequest, Object service) {
        Object result;
        try {
            Method method = service.getClass().getMethod(rpcRequest.getMethodName(), rpcRequest.getParamTypes());
            result = method.invoke(service, rpcRequest.getParameters());
            log.info("服务:[{}] 成功调用方法:[{}]", rpcRequest.getInterfaceName(), rpcRequest.getMethodName());
        } catch (NoSuchMethodException | IllegalArgumentException | InvocationTargetException | IllegalAccessException e) {
            throw new RpcException(e.getMessage(), e);
        }
        return result;
    }
}
