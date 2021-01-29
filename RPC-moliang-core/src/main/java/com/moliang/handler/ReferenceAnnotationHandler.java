package com.moliang.handler;

import com.moliang.annotation.RpcReference;
import com.moliang.convention.RpcServiceProperties;
import com.moliang.transport.RequestTransport;
import com.moliang.transport.netty.client.RpcClientProxy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

/**
 * @Use
 * @Author Chui moliang
 * @Date 2021/1/29 1:35
 * @Version 1.0
 */
@Slf4j
@Component
public class ReferenceAnnotationHandler implements BeanPostProcessor {
    private final RequestTransport rpcClient;

    @Autowired
    public ReferenceAnnotationHandler(RequestTransport rpcClient) {
        this.rpcClient = rpcClient;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> targetClass = bean.getClass();
        Field[] declaredFields = targetClass.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            RpcReference rpcReference = declaredField.getAnnotation(RpcReference.class);
            if (rpcReference != null) {
                RpcServiceProperties rpcServiceProperties = RpcServiceProperties.builder()
                        .group(rpcReference.group()).version(rpcReference.version()).build();
                RpcClientProxy rpcClientProxy = new RpcClientProxy(rpcClient, rpcServiceProperties);
                Object clientProxy = rpcClientProxy.getProxy(declaredField.getType());
                declaredField.setAccessible(true);
                try {
                    declaredField.set(bean, clientProxy);
                } catch (IllegalAccessException e) {
                    log.error(e.getMessage());
                }
            }

        }
        return bean;
    }
}
