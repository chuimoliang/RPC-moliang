package com.moliang.extension;

import com.moliang.annotation.RpcService;
import com.moliang.convention.RpcServiceProperties;
import com.moliang.factory.SingletonFactory;
import com.moliang.provider.ServiceProvider;
import com.moliang.provider.ServiceProviderImpl;
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

    public RegistryAnnotationHandler() {
        this.serviceProvider = SingletonFactory.getInstance(ServiceProviderImpl.class);
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
