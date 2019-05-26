package com.n33.jcu.utils.block.countdownlatch;

import java.util.concurrent.CountDownLatch;

/**
 * 线程之间相互等待
 *
 * @author N33
 * @date 2019/5/22
 */
public class CountDownLatchExample2 {


    public static void main(String[] args) throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);



        new Thread(() -> {
            System.out.println("Do some initial working.");
            try {
                Thread.sleep(1000);
                latch.await();
                System.out.println("Do other working...");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            System.out.println("asyn prepare for some data.");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                latch.countDown();
            }

        }).start();


        new Thread(() -> {
            try {
                latch.await();
                System.out.println("release.");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }).start();

        Thread.currentThread().join();


    }


}
