package com.kycode.jvm.stack;

/**
 * @author ：Kyara
 * @date ：Created in 2020/3/15 22:29
 * @description： TODO
 * @modified By：
 * @version: 1.0
 */
public class StackOverFlowDemo {
    public static long count = 0;

    public static void method(long i) {
        System.out.println(count++);
        method(i);
    }

    public static void main(String[] args) {
        method(1);
    }
}
