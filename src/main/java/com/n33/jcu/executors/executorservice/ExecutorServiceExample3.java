package com.n33.jcu.executors.executorservice;

import java.util.concurrent.*;
import java.util.stream.IntStream;

/**
 * 深入线程
 *
 * @author N33
 * @date 2019/6/1
 */
public class ExecutorServiceExample3 {


    public static void main(String[] args) throws InterruptedException {

//        test();

//        testAllowCoreThreadTimeOut();

//        testRemove();

//        testPrestartCoreThread();

//        testPrestartAllCoreThreads();

        testThreadPoolActive();
    }


    /**
     * 线程池一开始没有线程，当创建时才会有，但没超过最小不会被销毁
     *
     * @throws InterruptedException
     */
    private static void test() throws InterruptedException {
        final ThreadPoolExecutor executorService = (ThreadPoolExecutor) Executors.newFixedThreadPool(5);

        System.out.println(executorService.getActiveCount());

        executorService.execute(() -> {
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        TimeUnit.MILLISECONDS.sleep(20);

        System.out.println(executorService.getActiveCount());

    }


    /**
     * 回收线程，设置可以回收
     */
    private static void testAllowCoreThreadTimeOut() throws InterruptedException {
        ThreadPoolExecutor executorService = (ThreadPoolExecutor) Executors.newFixedThreadPool(5);

        //设置事件才可以回收，便可回收,也不用调shutdown
        executorService.setKeepAliveTime(3, TimeUnit.SECONDS);

        //Exception in thread "main" java.lang.IllegalArgumentException: Core threads must have nonzero keep alive times
        //回收时间不可以为零，否则报错
        //允许回收
        executorService.allowCoreThreadTimeOut(true);

        IntStream.range(0, 5).forEach(i -> {
            executorService.execute(() -> {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        });

        TimeUnit.SECONDS.sleep(10);

        //依旧可以执行
        executorService.execute(() -> System.out.println("=================="));
    }


    /**
     * 没被执行的任务可以被删掉
     *
     * @throws InterruptedException
     */
    private static void testRemove() throws InterruptedException {
        ThreadPoolExecutor executorService = (ThreadPoolExecutor) Executors.newFixedThreadPool(2);

        executorService.setKeepAliveTime(10, TimeUnit.SECONDS);
        executorService.allowCoreThreadTimeOut(true);

        IntStream.range(0, 2).forEach(i -> {
            executorService.execute(() -> {
                try {
                    TimeUnit.SECONDS.sleep(5);
                    System.out.println("========= i am finished");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        });

        TimeUnit.MILLISECONDS.sleep(20);

        Runnable r = () -> {
            System.out.println("I will never be executed!");
        };

        executorService.execute(r);
        //如果没有被执行，则杀死
        executorService.remove(r);
    }


    /**
     * 启动核心线程，导致它无所事事地等待工作。这将覆盖仅在执行新任务时启动核心线程的默认策略。
     * 如果所有核心线程都已启动，则此方法将返回{@code false}。
     *
     * @return {@code true} if a thread was started
     * <pre>
     *   public boolean prestartCoreThread() {
     *         return workerCountOf(ctl.get()) < corePoolSize &&
     *                 addWorker(null, true);
     *     }
     * </pre>
     */

    private static void testPrestartCoreThread() throws InterruptedException {
        ThreadPoolExecutor executorService = (ThreadPoolExecutor) Executors.newFixedThreadPool(2);
//        ThreadPoolExecutor executorService = new ThreadPoolExecutor(2, 4, 30, TimeUnit.SECONDS, new arrayblockingqueue<>(1));

        System.out.println(executorService.getActiveCount());

        System.out.println(executorService.prestartCoreThread());
        System.out.println(executorService.getActiveCount());

        executorService.execute(() -> {
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        TimeUnit.MILLISECONDS.sleep(20);

        //如果没有工作线程则不会启动，但是会执行，返回true,若果有，则不会启动，返回false
        System.out.println(executorService.prestartCoreThread());
        System.out.println(executorService.getActiveCount());

        IntStream.range(0, 2).forEach(i -> {
            executorService.execute(() -> {
                try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        });

        //超过core,则不会有效
        System.out.println(executorService.prestartCoreThread());
        System.out.println(executorService.getActiveCount());


    }


    /**
     * 启动所有核心线程，导致它们无所事事地等待工作。
     * 这将覆盖仅在执行新任务时启动核心线程的默认策略。
     *
     * @return the number of threads started
     * <pre>
     *      public int prestartAllCoreThreads() {
     *         int n = 0;
     *         while (addWorker(null, true))
     *             ++n;
     *         return n;
     *     }
     * </pre>
     */
    private static void testPrestartAllCoreThreads() throws InterruptedException {
        ThreadPoolExecutor executorService = (ThreadPoolExecutor) Executors.newFixedThreadPool(2);
        executorService.setMaximumPoolSize(3);

        System.out.println(executorService.getActiveCount());

        //直接启动CORE数量
        System.out.println(executorService.prestartAllCoreThreads());
        System.out.println("============" + executorService.getActiveCount());

        //没被使用会被清掉？？？
        System.out.println(executorService.prestartAllCoreThreads());
        System.out.println("============" + executorService.getActiveCount());
    }


    /**
     * 切面，任务执行生命周期？的意思吧
     */
    private static void testThreadPoolActive() {
        ThreadPoolExecutor executorService =
                new MyThreadPoolExecutor(1, 2, 30, TimeUnit.SECONDS, new ArrayBlockingQueue<>(1),
                        r -> {
                            Thread t = new Thread(r);
                            return t;
                        }, new ThreadPoolExecutor.AbortPolicy());

//        executorService.execute(new MyRunnable(1) {
//            @Override
//            public void run() {
//                System.out.println("==================");
//            }
//        });

        /**
         * init the 1
         * java.lang.ArithmeticException: / by zero 有错
         */
        executorService.execute(new MyRunnable(1) {
            @Override
            public void run() {
                System.out.println(1/0);
            }
        });

    }

    private abstract static class MyRunnable implements Runnable {

        private final int no;

        private MyRunnable(int no) {
            this.no = no;
        }

        protected int getData() {
            return this.no;
        }
    }


    private static class MyThreadPoolExecutor extends ThreadPoolExecutor {


        public MyThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
            super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
        }


        @Override
        protected void beforeExecute(Thread t, Runnable r) {
            System.out.println("init the " + ((MyRunnable)r).getData());
        }

        @Override
        protected void afterExecute(Runnable r, Throwable t) {
            if (null == t) {
                System.out.println("successful " + ((MyRunnable)r).getData());
            } else {
                t.printStackTrace();
            }
        }
    }

}
