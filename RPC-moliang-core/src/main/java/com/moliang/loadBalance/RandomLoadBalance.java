package com.moliang.loadBalance;

import java.util.List;
import java.util.Random;

/**
 * @Use
 * @Author Chui moliang
 * @Date 2021/1/24 22:02
 * @Version 1.0
 */
public class RandomLoadBalance implements LoadBalance {

    @Override
    public String selectServiceAddress(List<String> serviceAddresses, String rpcServiceName) {
        if (serviceAddresses == null || serviceAddresses.size() == 0) {
            return null;
        }
        if (serviceAddresses.size() == 1) {
            return serviceAddresses.get(0);
        }
        return doSelect(serviceAddresses, rpcServiceName);
    }

    protected String doSelect(List<String> serviceAddresses, String rpcServiceName) {
        Random random = new Random();
        return serviceAddresses.get(random.nextInt(serviceAddresses.size()));
    }
}
