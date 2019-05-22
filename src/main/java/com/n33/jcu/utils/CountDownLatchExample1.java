package com.n33.jcu.utils;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 等到全部完成才进行执行
 * 计数器门阀AQS
 *
 * @author N33
 * @date 2019/5/22
 */
public class CountDownLatchExample1 {

    private static Random random = new Random(System.currentTimeMillis());

    private static ExecutorService executor = Executors.newFixedThreadPool(2);

    private static final CountDownLatch latch = new CountDownLatch(10);


    public static void main(String[] args) throws InterruptedException {
        //(1)
        int[] data = query();
        //(2)
        for (int i = 0; i < data.length; i++) {
            executor.execute(new SimpleRunnable(data, i, latch));
        }
        //(3)
        latch.await();
        System.out.println("all of work finish done.");
        executor.shutdown();
        //shutdown()异步，awaitTermination（）阻塞
        //executor.awaitTermination(1, TimeUnit.HOURS);
        //System.out.println("all of work finish done.");
    }

    static class SimpleRunnable implements Runnable {

        private final int[] data;

        private final int index;

        private final CountDownLatch latch;

        public SimpleRunnable(int[] data, int index, CountDownLatch latch) {
            this.data = data;
            this.index = index;
            this.latch = latch;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(random.nextInt(2000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            int value = data[index];
            if (value % 2 == 0) {
                data[index] = value * 2;
            } else {
                data[index] = value * 10;
            }

            System.out.println(Thread.currentThread().getName() + " finished.");
            latch.countDown();
        }
    }


    private static int[] query() {
        return new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
    }


}
/**
 * 允许一个或多个线程等待的同步辅助
 *在其他线程中执行的一组操作完成。
 *
 * <p>使用给定的<em>计数</ em>初始化{@code CountDownLatch}。
 * {@link #await await}方法阻塞，直到达到当前计数
 *由于调用{@link #countDown}方法而归零，之后
 *释放所有等待的线程以及随后的任何调用
 * {@link #await await}立即返回。这是一次性现象
 *  - 计数无法重置。如果你需要一个重置版本的版本
 *计数，考虑使用{@link CyclicBarrier}。
 *
 *<p> {@code CountDownLatch}是一种多功能的同步工具
 *并可用于多种用途。一个
 * {@code CountDownLatch}初始化为一个计数作为一个
 *简单的开/关锁存器或门：所有线程调用{@link #await await}
 *在门口等待，直到它被一个调用{@link的线程打开
 * ＃倒数}。 {@code CountDownLatch}初始化为<em> N </ em>
 *可用于使一个线程等到<em> N </ em>线程有
 *完成了一些动作，或者某些动作已经完成了N次。
 *
 * <p> {@code CountDownLatch}的有用属性就是它
 *不要求调用{@code countDown}的线程等待
 *计数在继续之前达到零，它只是阻止任何
 *从{@link #await await}开始直到所有的线程
 *线程可以通过。
 *
 * <p> <b>样本用法：</ b>这是一对组中的一个类
 *工作线程使用两个倒计时锁存器：
 * <ul>
 * <li>第一个是启动信号，阻止任何工作人员继续进行
 *直到司机准备好继续进行;
 * <li>第二个是完成信号，允许驾驶员等待
 *直到所有工人完成。
 * </ ul>
 *
 *  <pre> {@code
 * class Driver { // ...
 *   void main() throws InterruptedException {
 *     CountDownLatch startSignal = new CountDownLatch(1);
 *     CountDownLatch doneSignal = new CountDownLatch(N);
 *
 *     for (int i = 0; i < N; ++i) // create and start threads
 *       new Thread(new Worker(startSignal, doneSignal)).start();
 *
 *     doSomethingElse();            // don't let run yet
 *     startSignal.countDown();      // let all threads proceed
 *     doSomethingElse();
 *     doneSignal.await();           // wait for all to finish
 *   }
 * }
 *
 * class Worker implements Runnable {
 *   private final CountDownLatch startSignal;
 *   private final CountDownLatch doneSignal;
 *   Worker(CountDownLatch startSignal, CountDownLatch doneSignal) {
 *     this.startSignal = startSignal;
 *     this.doneSignal = doneSignal;
 *   }
 *   public void run() {
 *     try {
 *       startSignal.await();
 *       doWork();
 *       doneSignal.countDown();
 *     } catch (InterruptedException ex) {} // return;
 *   }
 *
 *   void doWork() { ... }
 * }}</pre>
 *
 * <p>另一种典型用法是将问题分成N个部分，
 *用执行该部分的Runnable描述每个部分
 *倒计时锁定，并将所有Runnables排队到
 *执行人。当所有子部件完成时，协调线程
 *将能够通过等待。 （当线程必须重复时
 *以这种方式倒数，而不是使用{@link CyclicBarrier}。）
 *
 *  <pre> {@code
 * class Driver2 { // ...
 *   void main() throws InterruptedException {
 *     CountDownLatch doneSignal = new CountDownLatch(N);
 *     Executor e = ...
 *
 *     for (int i = 0; i < N; ++i) // create and start threads
 *       e.execute(new WorkerRunnable(doneSignal, i));
 *
 *     doneSignal.await();           // wait for all to finish
 *   }
 * }
 *
 * class WorkerRunnable implements Runnable {
 *   private final CountDownLatch doneSignal;
 *   private final int i;
 *   WorkerRunnable(CountDownLatch doneSignal, int i) {
 *     this.doneSignal = doneSignal;
 *     this.i = i;
 *   }
 *   public void run() {
 *     try {
 *       doWork(i);
 *       doneSignal.countDown();
 *     } catch (InterruptedException ex) {} // return;
 *   }
 *
 *   void doWork() { ... }
 * }}</pre>
 *
 * <p>内存一致性效果：直到计数达到
 *零，调用前线程中的操作
 * {@code countDown（）}
 * <a href="package-summary.html#MemoryVisibility"> <i>发生在之前</ i> </a>
 *从相应的成功返回后的行动
 * {@code await（）}在另一个线程中。
 *
 * @since 1.5
 * @author Doug Lea
 * public class CountDownLatch {
 */
