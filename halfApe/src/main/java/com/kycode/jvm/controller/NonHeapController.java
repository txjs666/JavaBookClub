package com.kycode.jvm.controller;


import com.google.common.collect.Lists;

import java.util.List;

/**
 * @author ：Kyara
 * @date ：Created in 2020/2/8 11:54
 * @description： TODO
 * @modified By：
 * @version: 1.0
 */
public class NonHeapController {
    List<Class<?>> list = Lists.newArrayList();

    public String heap() throws InterruptedException {
        while (true) {
//            list.addAll(MetaspaceUtil.createClasses());
            Thread.sleep(5);
        }
    }
}
