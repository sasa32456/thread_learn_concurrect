package com.n33.jcu.atomic;

import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

public class AtomicIntegerDetailTest {

    @Test
    public void testCreate() {
        /**
         * create
         */
        AtomicInteger i = new AtomicInteger();
        System.out.println(i.get());
        i = new AtomicInteger(10);
        System.out.println(i.get());

        //set
        i.set(12);
        System.out.println(i.get());
        //i.lazySet(13);//设置，用的时候才会真正设置

        //getAndAdd
        AtomicInteger getAndAdd = new AtomicInteger(10);
        final int result = getAndAdd.getAndAdd(10);
        System.out.println(result);
        System.out.println(getAndAdd.get());

        //addAndGet
        final AtomicInteger addAndGet2 = new AtomicInteger();
        new Thread(() -> {
            for (int j = 0; j < 10; j++) {
                final int v = addAndGet2.addAndGet(1);
                System.out.println(v);
            }
        }).start();
        new Thread(() -> {
            for (int j = 0; j < 10; j++) {
                final int v = addAndGet2.addAndGet(1);
                System.out.println(v);
            }
        }).start();


        System.out.println("---------------------");

        //getAndSet
        final AtomicInteger getAndSet = new AtomicInteger(10);
        System.out.println(getAndSet.getAndSet(1));
        System.out.println(getAndSet.get());

        System.out.println("---------------------");

        //getAndIncrement
        final AtomicInteger getAndIncrement = new AtomicInteger(10);
        System.out.println(getAndIncrement.getAndIncrement());
        System.out.println(getAndIncrement.get());

    }

    @Test
    public  void testCreate2() {

        AtomicInteger i = new AtomicInteger(10);
        i.compareAndSet(12, 20);
        System.out.println(i.get());


    }


    @Test
    public  void getLockException() {


    }


}
