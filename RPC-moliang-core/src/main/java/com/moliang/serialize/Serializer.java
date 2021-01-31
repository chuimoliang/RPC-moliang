package com.moliang.serialize;

import com.moliang.extension.SPI;

/**
 * @Use 序列化接口
 * @Author Chui moliang
 * @Date 2021/1/21 19:15
 * @Version 1.0
 */
@SPI
public interface Serializer {
    /**
     * 序列化
     *
     * @param obj 要序列化的对象
     * @return 字节数组
     */
    byte[] serialize(Object obj);

    /**
     * 反序列化
     *
     * @param bytes 序列化后的字节数组
     * @param clazz 目标类
     * @param <T>   类的类型。
     * @return 反序列化的对象
     */
    <T> T deserialize(byte[] bytes, Class<T> clazz);
}
