package com.n33.jcu.executors.executorservice;

import java.util.concurrent.*;

/**
 * 拒绝策略
 *
 * @author N33
 * @date 2019/5/31
 */
public class ExecutorServiceExample2 {


    public static void main(String[] args) throws InterruptedException {
//        testAbortPolicy();
//        testDiscardPolicy();
//        testCallerRunsPolicy();
         testDiscardOldestPolicy();
    }


    /**
     * 直接无法提交
     *
     * @throws InterruptedException
     */
    private static void testAbortPolicy() throws InterruptedException {

        ExecutorService executorService = new ThreadPoolExecutor(1, 2, 30, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(1),
                r -> {
                    Thread t = new Thread(r);
                    return t;
                }
                , new ThreadPoolExecutor.AbortPolicy());

        testExecute(executorService);

        /**
         *Exception in thread "main" java.util.concurrent.RejectedExecutionException:
         */
        executorService.execute(() -> System.out.println("x"));
    }


    /**
     * 无任何提示
     *
     * @throws InterruptedException
     */
    private static void testDiscardPolicy() throws InterruptedException {
        ExecutorService executorService = new ThreadPoolExecutor(1, 2, 30, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(1),
                r -> {
                    Thread t = new Thread(r);
                    return t;
                }
                , new ThreadPoolExecutor.DiscardPolicy());

        testExecute(executorService);

        /**
         * noThing happened
         */
        executorService.execute(() -> System.out.println("x"));
        System.out.println("==============================");
    }


    /**
     * 将任务返回调用线程执行
     */
    private static void testCallerRunsPolicy() throws InterruptedException {
        ExecutorService executorService = new ThreadPoolExecutor(1, 2, 30, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(1),
                r -> {
                    Thread t = new Thread(r);
                    return t;
                }
                , new ThreadPoolExecutor.CallerRunsPolicy());

        testExecute(executorService);

        /**
         * main
         */
        executorService.execute(() -> {
            System.out.println("x");
            System.out.println(Thread.currentThread().getName());
        });
        System.out.println("==============================");
    }


    private static void testExecute(ExecutorService executorService) throws InterruptedException {
        for (int i = 0; i < 3; i++) {
            executorService.execute(() -> {
                try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
        TimeUnit.SECONDS.sleep(1);
    }

    /**
     * 丢弃队列中的，执行现有的
     */
    private static void testDiscardOldestPolicy() throws InterruptedException {
        ExecutorService executorService = new ThreadPoolExecutor(1, 2, 30, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(1),
                r -> {
                    Thread t = new Thread(r);
                    return t;
                }
                , new ThreadPoolExecutor.DiscardOldestPolicy());

        for (int i = 0; i < 3; i++) {
            executorService.execute(() -> {
                try {
                    TimeUnit.SECONDS.sleep(5);
                    System.out.println("I come from lambda. " + Thread.currentThread().getName());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
        TimeUnit.SECONDS.sleep(1);

        /**
         * 队列中的被替换为现有的
         */
        executorService.execute(() -> {
            System.out.println("x");
            System.out.println(Thread.currentThread().getName());
        });
        System.out.println("==============================");
    }

}
/**
 * {@link ThreadPoolExecutor}无法执行的任务的处理程序。
 * <p>
 * public interface RejectedExecutionHandler {}
 * <p>
 * <p>
 * 1.
 * 拒绝任务的处理程序抛出{@code RejectedExecutionException}。
 * <p>
 * public static class AbortPolicy implements RejectedExecutionHandler {}
 * <p>
 * 2.
 * 被拒绝任务的处理程序，它直接在{@code execute}方法的调用线程中运行被拒绝的任务，
 * 除非执行程序已关闭，在这种情况下任务将被丢弃。
 * public static class CallerRunsPolicy implements RejectedExecutionHandler {}
 * <p>
 * 3.
 * 被拒绝任务的处理程序，它丢弃最早的未处理请求，然后重试{@code execute}，除非执行程序被关闭，在这种情况下任务被丢弃。
 * public static class DiscardOldestPolicy implements RejectedExecutionHandler {
 * <p>
 * <p>
 * 获取并忽略执行程序否则将执行的下一个任务（如果一个可立即执行），
 * 然后重试任务r的执行，除非执行程序被关闭，在这种情况下，任务r被丢弃。
 * <pre>
 * @param r请求执行的可运行任务@param e尝试执行此任务的执行程序
 * public void rejectedExecution(Runnable r,ThreadPoolExecutor e){
 *                  if(!e.isShutdown()){
 *                      e.getQueue().poll();
 *                      e.execute(r);
 *                  }
 *              }
 *           }
 * </pre>
 * <p>
 * 4.
 * 拒绝任务的处理程序，以静默方式丢弃被拒绝的任务。
 * public static class DiscardPolicy implements RejectedExecutionHandler {
 */

