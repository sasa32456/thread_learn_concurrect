package com.n33.jcu.utils.exchanger;

import java.util.concurrent.Exchanger;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * exchange之间交换数据
 * 1.如果线程没被交换就一直WAITing
 * 2.只适合两个线程去做。。多了难以处理
 * 3.
 *
 *
 * @author N33
 * @date 2019/5/23
 */
public class ExChangeExample1 {


    public static void main(String[] args) {

        final Exchanger<String> exchanger = new Exchanger<>();

        new Thread(()->{
            System.out.println(Thread.currentThread().getName() + " start.");
            try {
                final String result = exchanger.exchange("I am come T-A",1,TimeUnit.SECONDS);
                System.out.println(Thread.currentThread().getName() + " Get value ["+ result+"]");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
                System.out.println("Time out");
            }
            System.out.println(Thread.currentThread().getName() + " end.");
        },"==A==").start();

        new Thread(()->{
            System.out.println(Thread.currentThread().getName() + " start.");
            try {
                //休眠后会被等待交换
                TimeUnit.SECONDS.sleep(2);
                final String result = exchanger.exchange("I am come T-B");
                System.out.println(Thread.currentThread().getName() + " Get value ["+ result+"]");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + " end.");
        },"==B==").start();


//        /**
//         * 会和A交换。。B休眠被抛弃，B醒来等待交换
//         */
//        new Thread(()->{
//            System.out.println(Thread.currentThread().getName() + " start.");
//            try {
//                final String result = exchanger.exchange("I am come T-C");
//                System.out.println(Thread.currentThread().getName() + " Get value ["+ result+"]");
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            System.out.println(Thread.currentThread().getName() + " end.");
//        },"==C==").start();
//
//
//
//        /**
//         * 找时间相近的交换
//         * 交换期间阻塞线程?
//         */
//        new Thread(()->{
//            System.out.println(Thread.currentThread().getName() + " start.");
//            try {
//                TimeUnit.SECONDS.sleep(2);
//                final String result = exchanger.exchange("I am come T-D");
//                System.out.println(Thread.currentThread().getName() + " Get value ["+ result+"]");
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            System.out.println(Thread.currentThread().getName() + " end.");
//        },"==D==").start();

    }
}

/**
 * 线程可以配对和交换元素的同步点
 * 成对。每个线程都会在进入时呈现一些对象
 * {@link #exchange exchange}方法，与合作伙伴线程匹配，
 * 并在返回时收到其合作伙伴的对象。交换者可能是
 * 被视为{@link SynchronousQueue}的双向形式。
 * 交换器可能在遗传算法等应用中很有用
 * 和管道设计。
 *
 * <p> <b>样本用法：</ b>
 * 以下是使用{@code Exchanger}的类的亮点
 * 在线程之间交换缓冲区，以便线程填充
 * 缓冲区在需要时会获得一个刚刚清空的缓冲区
 * 填充一个到清空缓冲区的线程。
 * <pre> {@code
 * class FillAndEmpty {
 *   Exchanger<DataBuffer> exchanger = new Exchanger<DataBuffer>();
 *   DataBuffer initialEmptyBuffer = ... a made-up type
 *   DataBuffer initialFullBuffer = ...
 *
 *   class FillingLoop implements Runnable {
 *     public void run() {
 *       DataBuffer currentBuffer = initialEmptyBuffer;
 *       try {
 *         while (currentBuffer != null) {
 *           addToBuffer(currentBuffer);
 *           if (currentBuffer.isFull())
 *             currentBuffer = exchanger.exchange(currentBuffer);
 *         }
 *       } catch (InterruptedException ex) { ... handle ... }
 *     }
 *   }
 *
 *   class EmptyingLoop implements Runnable {
 *     public void run() {
 *       DataBuffer currentBuffer = initialFullBuffer;
 *       try {
 *         while (currentBuffer != null) {
 *           takeFromBuffer(currentBuffer);
 *           if (currentBuffer.isEmpty())
 *             currentBuffer = exchanger.exchange(currentBuffer);
 *         }
 *       } catch (InterruptedException ex) { ... handle ...}
 *     }
 *   }
 *
 *   void start() {
 *     new Thread(new FillingLoop()).start();
 *     new Thread(new EmptyingLoop()).start();
 *   }
 * }}</pre>
 *
 * <p>内存一致性效果：对于每对线程
 * 通过{@code Exchanger}动作成功交换对象
 * 在每个线程中的{@code exchange（）}之前
 * <a href="package-summary.html#MemoryVisibility"> <i>发生之前</ i> </a>
 * 从相应的{@code exchange（）}返回后的那些
 * 在另一个线程中。
 * <p>
 * public class Exchanger<V> {
 */
