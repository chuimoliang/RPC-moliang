package com.moliang.entity;

import com.moliang.convention.enums.RpcResponseCode;
import lombok.*;

import java.io.Serializable;

/**
 * @Use
 * @Author Chui moliang
 * @Date 2021/1/21 19:45
 * @Version 1.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Getter
@Setter
public class RpcResponse<T> implements Serializable {

    private static final long serialVersionUID = 715745410605631233L;

    private String requestId;

    private Integer code;

    private String message;

    private T data;

    public static <T> RpcResponse<T> success(T data, String requestId) {
        RpcResponse<T> response = new RpcResponse<>();
        response.setCode(RpcResponseCode.SUCCESS.getCode());
        response.setMessage(RpcResponseCode.SUCCESS.getMessage());
        response.setRequestId(requestId);
        if (null != data) {
            response.setData(data);
        }
        return response;
    }

    public static <T> RpcResponse<T> fail(RpcResponseCode rpcResponseCodeEnum) {
        RpcResponse<T> response = new RpcResponse<>();
        response.setCode(rpcResponseCodeEnum.getCode());
        response.setMessage(rpcResponseCodeEnum.getMessage());
        return response;
    }

}
