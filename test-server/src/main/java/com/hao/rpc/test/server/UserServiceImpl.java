package com.hao.rpc.test.server;

import com.hao.rpc.api.UserService;

public class UserServiceImpl implements UserService {
    @Override
    public String login(String name) {
        return name + " login !!!";
    }
}
