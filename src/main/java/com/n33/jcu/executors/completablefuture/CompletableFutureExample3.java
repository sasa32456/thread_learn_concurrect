package com.n33.jcu.executors.completablefuture;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * 中转方法
 *
 * @author N33
 * @date 2019/6/4
 */
public class CompletableFutureExample3 {


    public static void main(String[] args) throws Exception {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> "HELLO");

    }


    /**
     * 连续执行
     */
    private static void testThenRun() {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> "HELLO");
        future.thenRun(() -> System.out.println("done")).thenRunAsync(() -> System.out.println("done"));
    }


    /**
     * customer
     */
    private static void testThenAccept() {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> "HELLO");
        future.thenAcceptAsync(System.out::println).whenComplete((s, t) -> System.out.println(s));
    }


    /**
     * 返回自己
     */
    private static void testToCompletableFuture() {
        /**
         * Returns this CompletableFuture.
         *
         * @return this CompletableFuture
         * <pre>
         * public CompletableFuture<T> toCompletableFuture() {
         *             return this;
         *         }
         * </pre>
         */
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> "HELLO");
        future.toCompletableFuture();
    }


    /**
     * 与whenComplete进出一样作对比
     * 转为其他类型，有异常处理
     */
    private static void testHandle() throws InterruptedException, ExecutionException {

        final CompletableFuture<Integer> future = CompletableFuture.supplyAsync(
                (Supplier<String>) () -> {
                    throw new RuntimeException();
                })
                .handleAsync((s, t) -> {
                    Optional.of(t).ifPresent(e -> System.out.println("Error"));
                    return (s == null) ? 0 : s.length();
                });

        System.out.println(future.get());
    }


    /**
     * 转为其他类型，无异常处理
     *
     * @throws InterruptedException
     * @throws ExecutionException
     */
    private static void testThenApply() throws InterruptedException, ExecutionException {
        //        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> "Hello");

//        final CompletableFuture<Integer> integerCompletableFuture = future.thenApply(String::length);
//        System.out.println(integerCompletableFuture.get());

        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> "Hello").thenApplyAsync(s -> {
            try {
                System.out.println("=================");
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("=======over=======");
            return s.length();
        });
        System.out.println("================");
        System.out.println(future.get());

        Thread.currentThread().join();
    }


    /**
     * 执行完后发生
     *
     * @throws InterruptedException
     * @throws ExecutionException
     */
    private static void testWhenComplete() throws InterruptedException, ExecutionException {
        /**
         *  CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> "Hello");
         *  CompletableFuture<String> done = future.whenComplete((v, t) -> System.out.println("done"));
         *  <pre>等价</pre>
         *
         *  <pre>
         *  使用给定的编码结果创建新的完整CompletableFuture。
         *     private CompletableFuture(Object r) {
         *      this.result = r;
         *      }
         *  </pre>
         */
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> "Hello");

        //(方法)阻塞
        final CompletableFuture<String> done = future.whenComplete((v, t) -> {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("done");
        });
//        //非阻塞
//        future.whenCompleteAsync((s, throwable) -> {
//            try {
//                TimeUnit.SECONDS.sleep(2);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            System.out.println("=======over=======");
//        });
        System.out.println("================");
        System.out.println(future.get());

        Thread.currentThread().join();
    }


}
