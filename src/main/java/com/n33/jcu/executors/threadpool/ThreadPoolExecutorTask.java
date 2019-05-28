package com.n33.jcu.executors.threadpool;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * 线程退出
 *
 * @author N33
 * @date 2019/5/28
 */
public class ThreadPoolExecutorTask {
    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService = new ThreadPoolExecutor(10, 20, 30, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(10), Thread::new,new ThreadPoolExecutor.AbortPolicy());
        IntStream.range(0, 20).forEach(i->
            executorService.execute(() -> {
                try {
                    TimeUnit.SECONDS.sleep(2);
                    System.out.println(Thread.currentThread().getName() + " [" + i + "] finish done.");
                } catch (InterruptedException e) {
//                    e.printStackTrace();
                }}
            )
        );

//        //非阻塞
//        executorService.shutdown();
//        //阻塞
//        executorService.awaitTermination(1,TimeUnit.HOURS);
//        System.out.println("================over====================");

        //非阻塞
        List<Runnable> runnables = null;
        try {
            runnables = executorService.shutdownNow();
        } catch (Exception e) {
//            e.printStackTrace();
        }
        System.out.println("================over====================");
        System.out.println(runnables);
        System.out.println(runnables.size());
        /**
         * 尝试停止所有正在执行的任务，停止等待任务的处理，
         * 并返回等待执行的任务列表。从此方法返回时，这些任务将从任务队列中排空（删除）。
         * <p>此方法不等待主动执行任务终止。使用{@link #awaitTermination awaitTermination}来做到这一点。
         * <p>除了尽力尝试停止处理主动执行任务之外，没有任何保证。此实现通过{@link Thread＃interrupt}取消任务
         * ，因此任何无法响应中断的任务都可能永远不会终止。
         *
         * @throws SecurityException {@inheritDoc}
         *  public List<Runnable> shutdownNow() {
         */



            /**
             * shutdown
             * 20 threads
             *      10 threads work
             *      10 threads idle
             *
             * shutdown invoken
             *      10 waiting to finished the work.
             *      20 interruped.
             *      20 idle threads will exist
             *
             *
             *  shutdownNow
             *
             *  10 threads queue elements 10
             *  10 running
             *  10 stored in the blocking queue.
             *
             *  1.return list<Runnable> remain 10 un handle runnable.
             *  2.still work for the runnable by core thread.
             *
             */


            //other ...

        }
    }
