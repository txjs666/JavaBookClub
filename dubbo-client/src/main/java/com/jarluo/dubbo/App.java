package com.jarluo.dubbo;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Hello world!
 *
 */
public class App {
    public static void main( String[] args ) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("dubbo-client.xml");
        IJarLuoHello jarLuoHello = (IJarLuoHello) context.getBean("jarluoHelloService");
        System.out.println(jarLuoHello.sayHello("Jarluo"));
    }

}
