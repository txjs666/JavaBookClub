package com.kyara.kycode.jvm.jstack;

/**
 * @author ：Kyara
 * @date ：Created in 2020/3/15 22:19
 * @description： 运行主类
 * @modified By：
 * @version: 1.0
 */
public class DeadLockDemo {
    public static void main(String[] args) {
        DeadLock d1 = new DeadLock(true);
        DeadLock d2 = new DeadLock(false);
        Thread t1 = new Thread(d1);
        Thread t2 = new Thread(d2);
        t1.start();
        t2.start();
    }

}

//定义锁对象
class MyLock {
    public static Object object1 = new Object();
    public static Object object2 = new Object();
}

class DeadLock implements Runnable {
    private boolean flag;

    DeadLock(boolean flag) {
        this.flag = flag;
    }

    @Override
    public void run() {
        if (flag) {
            while (true) {
                synchronized (MyLock.object1) {
                    System.out.println(Thread.currentThread().getName() + "--if获得object1锁");
                    synchronized (MyLock.object2) {
                        System.out.println(Thread.currentThread().getName() + "--if获得object2锁");
                    }
                }
            }
        } else {
            while (true) {
                synchronized (MyLock.object2) {
                    System.out.println(Thread.currentThread().getName() + "--if获得object2锁");
                    synchronized (MyLock.object1) {
                        System.out.println(Thread.currentThread().getName() + "--if获得object1锁");
                    }
                }
            }
        }
    }
}
