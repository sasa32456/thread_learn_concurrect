package com.n33.jcu.atmoic;

import java.util.concurrent.atomic.AtomicBoolean;

public class AtomicBooleanFlag {


    /**
     * 替代volatile flag 内存消耗略大于前者
     */
    private final static AtomicBoolean flag = new AtomicBoolean(true);


    public static void main(String[] args) throws InterruptedException {

        new Thread(()->{
            while (flag.get()) {
                try {
                    Thread.sleep(1000);
                    System.out.println("I am working.");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            System.out.println("I am finished.");
        }).start();

        Thread.sleep(5000);


        flag.set(false);

    }

}
