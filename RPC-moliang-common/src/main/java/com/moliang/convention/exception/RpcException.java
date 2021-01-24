package com.moliang.convention.exception;

/**
 * @Use
 * @Author Chui moliang
 * @Date 2021/1/21 22:42
 * @Version 1.0
 */
public class RpcException extends RuntimeException {
    public RpcException(RpcErrorCode rpcErrorMessageEnum, String detail) {
        super(rpcErrorMessageEnum.getMessage() + ":" + detail);
    }

    public RpcException(String message, Throwable cause) {
        super(message, cause);
    }

    public RpcException(RpcErrorCode rpcErrorMessageEnum) {
        super(rpcErrorMessageEnum.getMessage());
    }
}
