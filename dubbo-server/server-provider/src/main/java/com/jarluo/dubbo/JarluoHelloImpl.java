package com.jarluo.dubbo;

/**
 * Hello world!
 *
 */
public class JarluoHelloImpl implements IJarLuoHello{

    @Override
    public String sayHello(String msg) {
        return "Hello:"+msg;
    }
}
