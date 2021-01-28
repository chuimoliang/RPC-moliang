package com.moliang.annotion;

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
     * Service version, default value is empty string
     */
    String version() default "";

    /**
     * Service group, default value is empty string
     */
    String group() default "";

}
