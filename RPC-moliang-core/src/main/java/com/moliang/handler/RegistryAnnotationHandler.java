package com.moliang.handler;

import com.moliang.annotation.RpcService;
import com.moliang.convention.RpcServiceProperties;
import com.moliang.provider.ServiceProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 * @Use
 * @Author Chui moliang
 * @Date 2021/1/29 1:30
 * @Version 1.0
 */
@Slf4j
@Component
public class RegistryAnnotationHandler implements BeanPostProcessor {

    private final ServiceProvider serviceProvider;

    @Autowired
    public RegistryAnnotationHandler(ServiceProvider serviceProvider) {
        this.serviceProvider = serviceProvider;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean.getClass().isAnnotationPresent(RpcService.class)) {
            log.info("[{}] 带有注释  [{}]", bean.getClass().getName(), RpcService.class.getCanonicalName());
            RpcService rpcService = bean.getClass().getAnnotation(RpcService.class);
            RpcServiceProperties rpcServiceProperties = RpcServiceProperties.builder()
                    .group(rpcService.group()).version(rpcService.version()).build();
            serviceProvider.publishService(bean, rpcServiceProperties);
        }
        return bean;
    }
}
