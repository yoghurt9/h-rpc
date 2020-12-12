package com.hao.rpc.loadBalance.impl;

import com.hao.rpc.loadBalance.LoadBalancer;

import java.net.InetSocketAddress;
import java.util.List;

public class RoundRobinLoadBalancer implements LoadBalancer {

    private int index = 0;

    @Override
    public InetSocketAddress select(List<InetSocketAddress> list) {
        if(index >= list.size()) {
            index = 0;
        }
        return list.get(index++);
    }
}