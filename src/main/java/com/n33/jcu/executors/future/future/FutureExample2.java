package com.n33.jcu.executors.future.future;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * future is cannel
 * 与get超时不同，try内不会接着执行
 *
 * @author N33
 * @date 2019/6/2
 */
public class FutureExample2 {


    public static void main(String[] args) throws Exception {
//        testIsDone();
//        testCannel();
        testCannel2();
    }


    /**
     * 如果此任务完成，则返回{@code true}。
     * 完成可能是由于正常终止，异常或取消 - 在所有这些情况下，此方法将返回{@code true}。
     */
    private static void testIsDone() throws ExecutionException, InterruptedException {
        ThreadPoolExecutor executorService = (ThreadPoolExecutor) Executors.newCachedThreadPool();

//        final Future<Integer> future = executorService.submit(() -> {
//            try {
//                TimeUnit.SECONDS.sleep(3);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            return 10;
//        });


//        final Integer result = future.get();
//        System.out.println(result + " is done "+future.isDone());

        final Future<Integer> future = executorService.submit(() -> {
            throw new RuntimeException();
        });

        try {
            final Integer result = future.get();
        } catch (Exception e) {
            System.out.println(" is done " + future.isDone());

        }
    }


    /**
     * 尝试取消执行此任务。如果任务已经完成，已被取消或由于某些其他原因无法取消，
     * 则此尝试将失败。如果成功，并且在调用{@code cancel}时此任务尚未启动，则此任务永远不会运行。
     * 如果任务已经启动，则{@code mayinterruptifrunning}参数确定执行此任务的线程是否应该在尝试停止任务时被中断。
     *
     * <p>此方法返回后，后续调用isDone即可 总是返回{@code true}。随后调用isCancelled
     * 如果此方法返回{@code true}，将始终返回{@code true}。
     * <p>
     * try to cancel maybe failed
     * <ul>
     * <li>task is completed already</li>
     * <li>has already been cancelled</li>
     * </ul>
     */
    private static void testCannel() throws ExecutionException, InterruptedException {

        ThreadPoolExecutor executorService = (ThreadPoolExecutor) Executors.newCachedThreadPool();

        AtomicBoolean running = new AtomicBoolean(true);

//        final Future<Integer> future = executorService.submit(() -> 10);
//
//        System.out.println(future.get());
//        future.cancel(true);



        //====================================================

//        Future<Integer> future = executorService.submit(() -> {
//            try {
//                TimeUnit.SECONDS.sleep(10);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            return 10;
//        });
//
//        TimeUnit.MILLISECONDS.sleep(10);
//
//        System.out.println(future.cancel(true));
//        System.out.println(future.cancel(true));

        //=======================================================


        Future<Integer> future = executorService.submit(() -> {
            while (running.get()) {

            }
            return 10;
        });

        TimeUnit.MILLISECONDS.sleep(10);

        System.out.println(future.cancel(true));
//        System.out.println(future.cancel(true));
        System.out.println(future.isDone());
        System.out.println(future.isCancelled());

    }


    private static void testCannel2() throws ExecutionException, InterruptedException {

        ThreadPoolExecutor executorService = (ThreadPoolExecutor) Executors.newCachedThreadPool();
//        ThreadPoolExecutor executorService = (ThreadPoolExecutor) Executors.newCachedThreadPool(new ThreadFactory() {
//            @Override
//            public Thread newThread(Runnable r) {
//                Thread t = new Thread(r);
//                t.setDaemon(true);
//                return t;
//            }
//        });
//
//        AtomicBoolean running = new AtomicBoolean(true);

//        Future<Integer> future = executorService.submit(() -> {
//            try {
//                TimeUnit.SECONDS.sleep(1);
//                System.out.println("=========================");
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            System.out.println("1111111111111111111111");
//            return 10;
//        });

        //================================================

//        Future<Integer> future = executorService.submit(() -> {
//            while (!Thread.interrupted()) {
//            }
//            System.out.println("1111111111111111111111");
//            return 10;
//        });
//
//        TimeUnit.MILLISECONDS.sleep(10);
//
//        System.out.println(future.cancel(true));
//        System.out.println(future.isDone());
//        System.out.println(future.isCancelled());

        //======================================================

//        Future<Integer> future = executorService.submit(() -> {
////            while (!Thread.interrupted()) {
////            }
//            while (running.get()) {
//            }
//            System.out.println("1111111111111111111111");
//            return 10;
//        });
//
//        TimeUnit.MILLISECONDS.sleep(10);
//
//        System.out.println(future.cancel(true));
//        System.out.println(future.isDone());
//        System.out.println(future.isCancelled());


        //======================================================


        Future<Integer> future = executorService.submit(() -> {
            while (!Thread.interrupted()) {
            }
            System.out.println("1111111111111111111111");
            return 10;
        });
        TimeUnit.MILLISECONDS.sleep(10);
        System.out.println(future.cancel(true));
        System.out.println(future.isDone());
        System.out.println(future.isCancelled());
        TimeUnit.MILLISECONDS.sleep(20);
        //拿不到,不返回结果
        //Exception in thread "main" java.util.concurrent.CancellationException
        System.out.println(future.get());
    }

}

