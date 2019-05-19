package com.n33.jcu.atmoic;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
/**
 * 赋予线程安全
 *
 * @author N33
 * @date 2019/5/19
 */
public class AtomicIntegerFieldUpdaterTest {

    public static void main(String[] args) {
        /**
         * 类
         * 字段
         */
        AtomicIntegerFieldUpdater<TestMe> updater = AtomicIntegerFieldUpdater.newUpdater(TestMe.class, "i");

        TestMe me = new TestMe();

        for (int i = 0; i < 2; i++) {
            new Thread(() -> {
                final int MAX = 20;
                for (int j = 0; j < MAX; j++) {
                    final int v = updater.getAndIncrement(me);
                    System.out.println(Thread.currentThread().getName() + " => " + v);
                }
            }).start();
        }

    }



    static class TestMe {

        volatile int i;

    }

}
