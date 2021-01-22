package com.moliang.convention.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Use
 * @Author Chui moliang
 * @Date 2021/1/21 23:32
 * @Version 1.0
 */
@AllArgsConstructor
@Getter
public enum SerializationType {

    KRYO((byte) 0x01, "kryo"),
    PROTOSTUFF((byte) 0x02, "protostuff");

    private final byte code;
    private final String name;

    public static String getName(byte code) {
        for (SerializationType c : SerializationType.values()) {
            if (c.getCode() == code) {
                return c.name;
            }
        }
        return null;
    }

}
