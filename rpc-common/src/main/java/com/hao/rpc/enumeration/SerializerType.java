package com.hao.rpc.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 序列化器的类型, 供序列化器的具体实现类的  com.hao.rpc.serializer.CommonSerializer#getByCode(int code) 使用
 * 具体编号可以修改，但是成员变量名不要轻易修改
 */
// 相当于一个配置文件
@AllArgsConstructor
@Getter
public enum SerializerType {

    JSON(1);

    private final int code;

}
