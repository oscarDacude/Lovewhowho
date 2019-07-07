package com.oscar.maven.LovedRedis;

import java.io.Serializable;

/**
 * @author: @我没有三颗心脏
 * @create: 2018-05-30-下午 22:31
 */
@SuppressWarnings("serial")
public class Student implements Serializable{

    private String name;
    private int age;

    /**
     * 给该类一个服务类用于测试
     */
    public void service() {
        System.out.println("学生名字为：" + name);
        System.out.println("学生年龄为：" + age);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}