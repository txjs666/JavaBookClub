package com.jarluo.dubbo;

import jdk.nashorn.internal.runtime.logging.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import sun.applet.Main;

import java.io.IOException;

/**
 * @from: https://www.cnblogs.com/java333/
 * @desc: TODO
 * @author: jar luo
 * @date: 2019/6/28 22:07
 */
public class Bootstrap {
    public static void main(String[] args) throws IOException {
        ClassPathXmlApplicationContext context =
                new ClassPathXmlApplicationContext
                        ("dubbo-server.xml");
        context.start();
        //阻塞当前进程
        System.in.read();


    }
}
