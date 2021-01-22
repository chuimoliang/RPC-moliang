package com.moliang.serialize;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.moliang.convention.exception.SerializeException;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * @Use Kryo序列化 只支持Java语言
 * @Author Chui moliang
 * @Date 2021/1/21 19:12
 * @Version 1.0
 */
@Slf4j
public class KryoUtil implements Serializer {

    private final ThreadLocal<Kryo> kryoThreadLocal = ThreadLocal.withInitial(() -> {
        Kryo kryo = new Kryo();
        kryo.setReferences(true);
        kryo.setRegistrationRequired(false);
        return kryo;
    });

    /**
     * 序列化方法
     * @param obj 要序列化的对象
     * @return 序列化后的字节数组
     */
    @Override
    public byte[] serialize(Object obj) {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             Output output = new Output(byteArrayOutputStream)) {
            Kryo kryo = kryoThreadLocal.get();
            // Object->byte:将对象序列化为byte数组
            kryo.writeObject(output, obj);
            kryoThreadLocal.remove();
            return output.toBytes();
        } catch (Exception e) {
            throw new SerializeException("序列化失败");
        }
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
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
             Input input = new Input(byteArrayInputStream)) {
            Kryo kryo = kryoThreadLocal.get();
            // byte->Object:从byte数组中反序列化出对对象
            Object o = kryo.readObject(input, clazz);
            kryoThreadLocal.remove();
            return clazz.cast(o);
        } catch (Exception e) {
            throw new SerializeException("反序列化失败");
        }
    }

    /**
    public static void main(String[] args) {
        RPCRequest request = RPCRequest.builder()
                .requestId("sssssssssss")
                .group("fasd")
                .version("1.3")
                .interfaceName("333")
                .build();
        KryoUtil util = new KryoUtil();
        System.out.println(util.deserialize(util.serialize(request), RPCRequest.class));
    }
     */

}
