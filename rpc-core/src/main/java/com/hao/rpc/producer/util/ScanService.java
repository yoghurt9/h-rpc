package com.hao.rpc.producer.util;

import com.hao.rpc.annotation.RpcService;
import com.hao.rpc.annotation.ServiceScan;
import com.hao.rpc.enumeration.RpcError;
import com.hao.rpc.exception.RpcException;
import com.hao.rpc.producer.transport.RpcServer;
import com.hao.rpc.util.ReflectUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

@Slf4j
public class ScanService {

    private RpcServer rpcServer;

    public ScanService(RpcServer rpcServer) {
        this.rpcServer = rpcServer;
    }

    public void scan(String mainClassName) {
        Class<?> startClass;
        try {
            startClass = Class.forName(mainClassName);
            if(!startClass.isAnnotationPresent(ServiceScan.class)) {
                log.error("启动类缺少 @ServiceScan 注解");
                throw new RpcException(RpcError.SERVICE_SCAN_PACKAGE_NOT_FOUND);
            }
        } catch (ClassNotFoundException e) {
            log.error("出现未知错误");
            throw new RpcException(RpcError.UNKNOWN_ERROR);
        }
        String basePackage = startClass.getAnnotation(ServiceScan.class).value();
        if("".equals(basePackage)) {
            basePackage = mainClassName.substring(0, mainClassName.lastIndexOf("."));
        }
        Set<Class<?>> classSet = ReflectUtil.getClasses(basePackage);
        for(Class<?> clazz : classSet) {
            if(clazz.isAnnotationPresent(RpcService.class)) {
                String serviceName = clazz.getAnnotation(RpcService.class).name();
                Object obj;
                try {
                    obj = clazz.newInstance();
                } catch (InstantiationException | IllegalAccessException e) {
                    log.error("创建 " + clazz + " 时有错误发生");
                    continue;
                }
                if("".equals(serviceName)) {
                    Class<?>[] interfaces = clazz.getInterfaces();
                    for (Class<?> clazz0: interfaces){
                        rpcServer.register(obj, clazz0.getCanonicalName());
                    }
                } else {
                    rpcServer.register(obj, serviceName);
                }
            }
        }
    }
}
