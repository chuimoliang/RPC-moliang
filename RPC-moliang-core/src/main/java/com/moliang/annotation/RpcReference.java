package com.moliang.annotation;

import java.lang.annotation.*;

/**
 * @Use 服务消费者要引用的bean的注解
 * @Author Chui moliang
 * @Date 2021/1/26 21:40
 * @Version 1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Inherited
public @interface RpcReference {

    /**
     * 服务版本，默认值为空字符串
     */
    String version() default "";

    /**
     * 服务组，默认值为空字符串
     */
    String group() default "";

}
