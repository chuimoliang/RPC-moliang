package com.moliang.convention.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * @Use
 * @Author Chui moliang
 * @Date 2021/1/21 20:09
 * @Version 1.0
 */
@AllArgsConstructor
@Getter
@ToString
public enum RpcResponseCode {

    SUCCESS(200, "调用成功"),
    FAIL(500, "调用失败");

    private final int code;
    private final String message;
}
