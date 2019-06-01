package com.n33.jcu.utils.block.semaphore;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
/**
 *
 *
 *
 * @author N33
 * @date 2019/5/25
 */
public class SemaphoreExample3 {


    public static void main(String[] args) throws InterruptedException {

        final Semaphore semaphore = new Semaphore(1);

        final Thread t1 = new Thread(() -> {
            try {
                semaphore.acquire();
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                semaphore.release();
            }
            System.out.println("T1 finished");
        });
        t1.start();


        TimeUnit.MICROSECONDS.sleep(50);

        final Thread t2 = new Thread(() -> {
            try {
                //不会被中断
                semaphore.acquireUninterruptibly();
//                TimeUnit.SECONDS.sleep(2);
            } finally {
                semaphore.release();
            }
            System.out.println("T2 finished");
        });
        t2.start();

        TimeUnit.MICROSECONDS.sleep(50);
        t2.interrupt();

    }

}
