package com.zhanglin.utils;

import org.junit.jupiter.api.Test;

import java.util.UUID;

@SuppressWarnings("all")//抑制警告--Java原生
public class IDutils {
    public static String getId(){
        return UUID.randomUUID().toString().replaceAll("-","");
    }
    @Test
    public void test(){
        System.out.println(IDutils.getId());
    }
}
