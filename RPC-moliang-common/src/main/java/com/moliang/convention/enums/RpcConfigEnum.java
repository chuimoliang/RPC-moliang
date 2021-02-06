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
    LOAD_BALANCE_TYPE("rpc.loadBalance"),
    SERIALIZE_TYPE("rpc.serialize");

    private final String propertyValue;

}
