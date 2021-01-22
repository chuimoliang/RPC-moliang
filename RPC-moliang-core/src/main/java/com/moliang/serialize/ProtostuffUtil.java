package com.moliang.serialize;

import com.esotericsoftware.kryo.Kryo;
import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;

import java.util.Arrays;

/**
 * @Use
 * @Author Chui moliang
 * @Date 2021/1/21 21:44
 * @Version 1.0
 */
public class ProtostuffUtil implements Serializer {

    /**
     * 避免每次序列化重新申请空间 其中 DEFAULT_BUFFER_SIZE = 512;
     */
    //private static final LinkedBuffer BUFFER = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
    private final ThreadLocal<LinkedBuffer> protoThreadLocal = ThreadLocal.withInitial(() -> {
        LinkedBuffer BUFFER = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
        return BUFFER;
    });

    /**
     * 序列化方法
     * @param obj 要序列化的对象
     * @return 序列化后的字节数组
     */
    @Override
    public byte[] serialize(Object obj) {
        LinkedBuffer BUFFER = protoThreadLocal.get();
        Class<?> clazz = obj.getClass();
        Schema schema = RuntimeSchema.getSchema(clazz);
        byte[] bytes;
        try {
            bytes = ProtostuffIOUtil.toByteArray(obj, schema, BUFFER);
        } finally {
            BUFFER.clear();
        }
        return bytes;
    }

    /**
     * 反序列化方法
     * @param bytes 序列化后的字节数组
     * @param clazz 目标类
     * @param <T>
     * @return 反序列化后的对象
     */
    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        Schema<T> schema = RuntimeSchema.getSchema(clazz);
        T obj = schema.newMessage();
        ProtostuffIOUtil.mergeFrom(bytes, obj, schema);
        return obj;
    }

    /** 测试
    public static void main(String[] args) {
        new Thread(()->{
            String str = "System.out.println(p.deserialize(p.serialize(str), String.class));";
            ProtostuffUtil p = new ProtostuffUtil();
            System.out.println(p.deserialize(p.serialize(str), String.class));
        }).start();
        new Thread(()->{
            String str = "System.out.println(p.deserialize(p.serialize(str), String.class));";
            ProtostuffUtil p = new ProtostuffUtil();
            System.out.println(p.deserialize(p.serialize(str), String.class));
        }).start();
    }
     */
}
