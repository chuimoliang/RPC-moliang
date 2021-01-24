package com.moliang.convention;

import lombok.*;

/**
 * @Use
 * @Author Chui moliang
 * @Date 2021/1/21 19:45
 * @Version 1.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class RpcServiceProperties {

    private String version;
    /**
     * 通过group选择接口的实现
     */
    private String group;
    private String serviceName;

    public String toRpcServiceName() {
        return this.getServiceName() + this.getGroup() + this.getVersion();
    }
}
