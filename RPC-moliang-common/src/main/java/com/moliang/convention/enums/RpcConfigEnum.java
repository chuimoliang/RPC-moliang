package com.moliang.convention.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum RpcConfigEnum {

    RPC_CONFIG_PATH("rpc.properties"),
    RPC_CONFIG_YAML_PATH("rpc.yml"),
    SERVICE_PATTERN("rpc.service"),
    ZK_ADDRESS("rpc.zookeeper.address"),
    NACOS_ADDRESS("rpc.nacos.address"),

    LOAD_BALANCE_TYPE("rpc.loadBalance.mode"),
    ENCODE_SERIALIZE_TYPE("rpc.encode.mode"),
    DECODE_SERIALIZE_TYPE("rpc.decode.mode");

    private final String propertyValue;

}
