package com.moliang.entity;

import com.moliang.convention.RpcServiceProperties;
import lombok.*;

/**
 * @Use
 * @Author Chui moliang
 * @Date 2021/1/21 19:58
 * @Version 1.0
 */
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Builder
public class RpcRequest {
    private static final long serialVersionUID = 1905122041950251207L;
    private String requestId;
    private String interfaceName;
    private String methodName;
    private Object[] parameters;
    private Class<?>[] paramTypes;
    private String version;
    private String group;

    public RpcServiceProperties toRpcProperties() {
        return RpcServiceProperties.builder().serviceName(this.getInterfaceName())
                .version(this.getVersion())
                .group(this.getGroup()).build();
    }
}
