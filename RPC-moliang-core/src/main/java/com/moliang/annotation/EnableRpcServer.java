package com.moliang.annotation;

import com.moliang.utils.EnableServerUtil;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @Use
 * @Author Chui moliang
 * @Date 2021/2/4 17:11
 * @Version 1.0
 */
@Target(ElementType.TYPE)
@Inherited
@Import(EnableServerUtil.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface EnableRpcServer {
}
