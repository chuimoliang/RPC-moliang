package com.moliang.extension;

import com.moliang.annotation.RpcReference;
import com.moliang.convention.RpcServiceProperties;
import com.moliang.extension.ExtensionLoader;
import com.moliang.factory.SingletonFactory;
import com.moliang.transport.RequestTransport;
import com.moliang.transport.netty.client.NettyClient;
import com.moliang.transport.netty.client.RpcClientProxy;
import com.moliang.transport.netty.server.RequestHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

/**
 * @Use 代理 Bean 设置
 * @Author Chui moliang
 * @Date 2021/1/29 1:35
 * @Version 1.0
 */
@Slf4j
@Component
public class ReferenceAnnotationHandler implements BeanPostProcessor {
    private final RequestTransport rpcClient;

    public ReferenceAnnotationHandler() {
        log.info("客户端初始化");
        this.rpcClient = ExtensionLoader.getExtensionLoader(RequestTransport.class).getExtension("netty");
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        // 获得 bean 类型
        Class<?> targetClass = bean.getClass();
        Field[] declaredFields = targetClass.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            RpcReference rpcReference = declaredField.getAnnotation(RpcReference.class);
            if (rpcReference != null) {
                // 获得引用服务参数
                RpcServiceProperties rpcServiceProperties = RpcServiceProperties.builder()
                        .group(rpcReference.group()).version(rpcReference.version())
                        .asyncMode(rpcReference.async()).build();
                // 生成代理操作类
                RpcClientProxy rpcClientProxy = new RpcClientProxy(rpcClient, rpcServiceProperties);
                Object clientProxy = rpcClientProxy.getProxy(declaredField.getType());
                declaredField.setAccessible(true);
                try {
                    // 将实例设置为代理类型
                    declaredField.set(bean, clientProxy);
                } catch (IllegalAccessException e) {
                    log.error(e.getMessage());
                }
            }

        }
        return bean;
    }
}
