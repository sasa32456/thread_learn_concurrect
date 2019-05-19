package com.n33.jcu.atmoic;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

/**
 * 无锁
 *
 * @author N33
 * @date 2019/5/19
 */
public class ATFUTest {
    private volatile int i;

    private AtomicIntegerFieldUpdater<ATFUTest> updater = AtomicIntegerFieldUpdater.newUpdater(ATFUTest.class, "i");

    public void update(int newValue) {
        updater.compareAndSet(this, i, newValue);
    }

    public int get() {
        return i;
    }

    public static void main(String[] args) {
        ATFUTest test = new ATFUTest();


        test.update(10);
        System.out.println(test.get());


    }


}
