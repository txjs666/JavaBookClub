package com.kyara.kycode.jvm.controller;

import org.assertj.core.util.Lists;

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
