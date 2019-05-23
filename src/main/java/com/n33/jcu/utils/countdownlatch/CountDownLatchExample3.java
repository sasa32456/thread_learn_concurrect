package com.n33.jcu.utils.countdownlatch;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * CountDownLatch api
 *
 * @author N33
 * @date 2019/5/22
 */
public class CountDownLatchExample3 {
    public static void main(String[] args) throws InterruptedException {
        /**
         * 无作用
         */
        //final CountDownLatch latch = new CountDownLatch(0);

        /**
         * Exception in thread "main" java.lang.IllegalArgumentException: count < 0
         */
        //final CountDownLatch latch = new CountDownLatch(-1);
        final CountDownLatch latch = new CountDownLatch(1);

        final Thread mainThread = Thread.currentThread();

        new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //latch.countDown();
            /**
             * 当没人中断时候interrupt打断
             */
            //mainThread.interrupt();
        }).start();


        //设置等待时间
        latch.await(1000, TimeUnit.MICROSECONDS);
        System.out.println("===================");

        //已经为0就不会再执行
        latch.countDown();
    }
}
/**
 * 导致当前线程等待，直到锁存器倒计数到
 * 零，除非线程是{@linkplain Thread＃interrupt interrupted}。
 *
 * <p>如果当前计数为零，则此方法立即返回。
 *
 * <p>如果当前计数大于零则为当前计数
 * 线程被禁用用于线程调度和谎言
 * 休眠直到两件事之一发生：
 * <ul>
 * <li>由于调用，计数达到零
 * {@link #countDown}方法;要么
 * <li>其他一些帖子{@linkplain Thread＃interrupt interrupts}
 * 当前线程。
 * </ ul>
 *
 * <p>如果当前线程：
 * <ul>
 * <li>在进入此方法时设置了中断状态;要么
 * 等待时，<li>是{@linkplain线程＃中断中断}
 * </ ul>
 * 然后抛出{@link InterruptedException}和当前线程
 * 中断状态被清除。
 *
 * @throws InterruptedException如果当前线程被中断
 * 等待
 * public void await() throws InterruptedException {
 * sync.acquireSharedInterruptibly(1);
 * }
 */
