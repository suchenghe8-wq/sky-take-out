package com.sky.annotation;


import com.sky.enumeration.OperationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/*
* 自定义注解，用于标识某个方法需要进行功能字段的自动填充
* */
@Target(ElementType.METHOD)//指定注解位置，作用于方法
@Retention(RetentionPolicy.RUNTIME)//指定注解的生命周期
public @interface AutoFill {//声明自定义注解
    //数据库操作类型 UPDATE INSERT
    OperationType value();//定义注解的属性，更新和插入
}
