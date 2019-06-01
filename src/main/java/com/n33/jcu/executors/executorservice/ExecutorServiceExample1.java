package com.n33.jcu.executors.executorservice;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

/**
 * 异常处理
 *
 * @author N33
 * @date 2019/5/30
 */
public class ExecutorServiceExample1 {


    public static void main(String[] args) throws InterruptedException {

//        isShutDown();
//        isTerminated();
//        executeRunnableError();
        executeRunnableTask();

    }

    /**
     * {@link ExecutorService#isShutdown()}
     * shutdown后还能执行？
     * Exception in thread "main" java.util.concurrent.RejectedExecutionException:不行
     */
    private static void isShutDown() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        System.out.println(executorService.isShutdown());
        executorService.shutdown();
        System.out.println(executorService.isShutdown());
        executorService.execute(() -> System.out.println("i will be executed after shutdown?"));
    }


    /**
     * {@link ExecutorService#isTerminated()}
     * {@link java.util.concurrent.ThreadPoolExecutor#isTerminated()}
     * 是否已经被终结
     */
    private static void isTerminated() {
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        executorService.execute(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } );

        executorService.shutdown();
        System.out.println(executorService.isShutdown());
        System.out.println(executorService.isTerminated());
        //是否在shutdown过程中
        System.out.println(((ThreadPoolExecutor)executorService).isTerminating());
    }


    /**
     * |-->
     * send request ---> store db ---> 10 ->|-->
     * |-->
     *
     * @throws InterruptedException
     */
    private static void executeRunnableTask() throws InterruptedException {

        ExecutorService executorService = Executors.newFixedThreadPool(10,new MyThreadFactory());
        IntStream.range(0, 10).forEach(i -> executorService.execute(
                new MyTask(i) {
                    @Override
                    protected void error(Throwable cause) {
                        System.out.println("The no: " + i+" failed, update status to ERROR");
                    }

                    @Override
                    protected void done() {
                        System.out.println("The no: " + i+" successfully, update status to DONE");
                    }

                    @Override
                    protected void doExecute() {
                        if (i % 3 == 0) {
                            int tmp = 1 / 0;
                        }
                    }

                    @Override
                    protected void doInit() {
                        //do nothing
                    }
                }
        ));
        executorService.shutdown();
        executorService.awaitTermination(10, TimeUnit.MINUTES);
        System.out.println("================================");

    }

    /**
     * 模板设计模式
     */
    private abstract static class MyTask implements Runnable {

        private final int no;

        public MyTask(int no) {
            this.no = no;
        }

        @Override
        public void run() {
            try {
                this.doInit();
                this.doExecute();
                this.done();
            } catch (Throwable cause) {
                this.error(cause);
            }
        }

        protected abstract void error(Throwable cause);

        protected abstract void done();

        protected abstract void doExecute();

        protected abstract void doInit();

    }









    private static void executeRunnableError() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(10,new MyThreadFactory());
        IntStream.range(0, 10).forEach(i -> executorService.execute(() -> System.out.println(1 / 0)));
        executorService.shutdown();
        executorService.awaitTermination(10, TimeUnit.MINUTES);
        System.out.println("================================");
    }


    /**
     * 不好，复杂的Runnable没用
     */
    private static class MyThreadFactory implements ThreadFactory {
        private final static AtomicInteger SEQ = new AtomicInteger();

        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r);
            thread.setName("My-Thread-" + SEQ.getAndIncrement());
            thread.setUncaughtExceptionHandler((t, e) -> {
                System.out.println("The Thread " + t.getName()+ "execute failed.");
                e.printStackTrace();
                System.out.println("==========================================");
            });
            return thread;
        }
    }




}
/**
 * 提供{@link ExecutorService}执行方法的默认实现。
 * 此类使用{@code newTaskFor}返回的{@link RunnableFuture}实现{@code submit}，
 * {@ code invokeAny}和{@code invokeAll}方法，默认为此中提供的{@link FutureTask}类包。
 * 例如，{@code submit（Runnable）}的实现创建了一个执行并返回的关联{@code RunnableFuture}。
 * 子类可以覆盖{@code newTaskFor}方法，以返回{@code FutureTask}以外的{@code RunnableFuture}实现。
 *
 * <p><b>Extension example</b>. Here is a sketch of a class
 * that customizes {@link ThreadPoolExecutor} to use
 * a {@code CustomTask} class instead of the default {@code FutureTask}:
 *  <pre> {@code
 * public class CustomThreadPoolExecutor extends ThreadPoolExecutor {
 *
 *   static class CustomTask<V> implements RunnableFuture<V> {...}
 *
 *   protected <V> RunnableFuture<V> newTaskFor(Callable<V> c) {
 *       return new CustomTask<V>(c);
 *   }
 *   protected <V> RunnableFuture<V> newTaskFor(Runnable r, V v) {
 *       return new CustomTask<V>(r, v);
 *   }
 *   // ... add constructors, etc.
 * }}</pre>
 *
 * @since 1.5
 * @author Doug Lea
 * public abstract class AbstractExecutorService implements ExecutorService {
 */
