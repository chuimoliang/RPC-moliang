package com.moliang.annotation;

import java.lang.annotation.*;

/**
 * @Use 服务提供者注册服务所用注解
 * @Author Chui moliang
 * @Date 2021/1/29 1:37
 * @Version 1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Inherited
public @interface RpcService {
    /**
     * 服务版本，默认值为空字符串
     */
    String version() default "";

    /**
     * 服务组，默认值为空字符串
     */
    String group() default "";
}
