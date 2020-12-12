package com.hao.rpc.test.server.impl;

import com.hao.rpc.annotation.RpcService;
import com.hao.rpc.api.UserService;

@RpcService
public class UserServiceImpl implements UserService {
    @Override
    public String login(String name) {
        System.out.println(name + " login");
        return name + " login !!!";
    }
}
