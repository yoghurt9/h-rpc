package com.hao.rpc.loadBalance;


import java.net.InetSocketAddress;
import java.util.List;


/**
 * 策略模式
 */
public interface LoadBalancer {

    InetSocketAddress select(List<InetSocketAddress> list);

}