package com.moliang.transport.netty;

import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Use 通道控制类
 * @Author Chui moliang
 * @Date 2021/1/23 0:35
 * @Version 1.0
 */
@Slf4j
public class ChannelProvider {
    private final Map<String, Channel> channelMap;

    public ChannelProvider(Map<String, Channel> map) {
        this.channelMap = new ConcurrentHashMap<>();
    }

    public Channel get(InetSocketAddress inetSocketAddress) {
        String key = inetSocketAddress.toString();
        if (channelMap.containsKey(key)) {
            Channel channel = channelMap.get(key);
            if (channel != null && channel.isActive()) {
                return channel;
            } else {
                channelMap.remove(key);
            }
        }
        return null;
    }

    public void set(InetSocketAddress inetSocketAddress, io.netty.channel.Channel channel) {
        String key = inetSocketAddress.toString();
        channelMap.put(key, channel);
    }

    public void remove(InetSocketAddress inetSocketAddress) {
        String key = inetSocketAddress.toString();
        channelMap.remove(key);
        log.info("Channel map size :[{}]", channelMap.size());
    }
}
