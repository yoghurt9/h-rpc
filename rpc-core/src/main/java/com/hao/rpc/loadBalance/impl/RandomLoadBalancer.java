package com.hao.rpc.loadBalance.impl;

import com.hao.rpc.loadBalance.LoadBalancer;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Random;

public class RandomLoadBalancer implements LoadBalancer {

    @Override
    public InetSocketAddress select(List<InetSocketAddress> list) {
        return list.get(new Random().nextInt(list.size()));
    }

}