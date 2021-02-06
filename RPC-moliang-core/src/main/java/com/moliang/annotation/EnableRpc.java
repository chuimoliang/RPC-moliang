package com.moliang.annotation;

import com.moliang.extension.BeanScannerRegistry;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @Use
 * @Author Chui moliang
 * @Date 2021/1/29 23:08
 * @Version 1.0
 */
@Target(ElementType.TYPE)
@Inherited
@Import(BeanScannerRegistry.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface EnableRpc {

    /**
     * 需要扫描的包
     */
    String[] packages();
}
