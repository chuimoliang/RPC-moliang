package com.moliang.factory;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * @Use 获取单例对象的工厂类
 * @Author Chui moliang
 * @Date 2021/2/1 19:36
 * @Version 1.0
 */
@Slf4j
public class SingletonFactory {

    private static final Map<String, Object> MAP = new HashMap<>();

    private SingletonFactory() {}

    public static <T> T getInstance(Class<T> c) {
        String key = c.toString();
        Object instance;
        synchronized (SingletonFactory.class) {
            instance = MAP.get(key);
            if(instance == null) {
                try {
                    instance = c.getDeclaredConstructor().newInstance();
                    MAP.put(key, instance);
                } catch (Exception e) {
                    log.error(e.getMessage());
                }
            }
        }
        return c.cast(instance);
    }

}
