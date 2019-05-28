package com.n33.jcu.executors.threadpool;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

/**
 * @author N33
 * @date 2019/5/28
 */
public class ThreadPoolExecutorLongTimeTask {
    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService = new ThreadPoolExecutor(10, 20, 30, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(10),
                /**
                 * 自定义，守护线程守护main，main结束线程结束
                 */
                r -> {
                    Thread t = new Thread(r);
                    t.setDaemon(true);
                    return t;
                }
                , new ThreadPoolExecutor.AbortPolicy());

        //pall
        IntStream.range(0, 10).boxed().forEach(i->{
            executorService.submit(() -> {
                while (true) {

                }
            });
        });

        //seq
        //没有完成的依旧会完成，now会返回没完成的
//        executorService.shutdown();

        //也停不掉
        executorService.shutdownNow();

        executorService.awaitTermination(5, TimeUnit.SECONDS);
        System.out.println("============  start sequence work ===================");

    }
}
/**
 * <p>
 * static class DefaultThreadFactory implements ThreadFactory {
 *     private static final AtomicInteger poolNumber = new AtomicInteger(1);
 *     private final ThreadGroup group;
 *     private final AtomicInteger threadNumber = new AtomicInteger(1);
 *     private final String namePrefix;
 *
 *     DefaultThreadFactory() {
 *         SecurityManager s = System.getSecurityManager();
 *         group = (s != null) ? s.getThreadGroup() :
 *                 Thread.currentThread().getThreadGroup();
 *         namePrefix = "pool-" +
 *                 poolNumber.getAndIncrement() +
 *                 "-thread-";
 *     }
 *
 *     public Thread newThread(Runnable r) {
 *         Thread t = new Thread(group, r,
 *                 namePrefix + threadNumber.getAndIncrement(),
 *                 0);
 *         if (t.isDaemon())
 *         //这里设置不让成为守护，所以停不下来
 *             t.setDaemon(false);
 *         if (t.getPriority() != Thread.NORM_PRIORITY)
 *             t.setPriority(Thread.NORM_PRIORITY);
 *         return t;
 *     }
 * }
 *
 * </p>
 */
