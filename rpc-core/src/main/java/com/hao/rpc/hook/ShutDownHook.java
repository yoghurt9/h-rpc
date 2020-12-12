package com.hao.rpc.hook;

import com.hao.rpc.registry.ServiceRegistry;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ShutDownHook {

    private ServiceRegistry serviceRegistry;

    public ShutDownHook(ServiceRegistry serviceRegistry) {
        this.serviceRegistry = serviceRegistry;
    }

    public void addClearTask() {
        log.info("添加 shutdown hook, 在jvm退出时将自动执行");
        Runtime.getRuntime().addShutdownHook(new Thread(() -> serviceRegistry.clearRegistry()));
    }

}
