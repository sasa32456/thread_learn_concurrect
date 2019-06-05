package com.n33.jcu.executors.completablefuture;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * 不再返回future的终结方法
 *
 * 还有多点尝试，太零散
 *
 * @author N33
 * @date 2019/6/5
 */
public class CompletableFutureExample5 {

    public static void main(String[] args) throws Exception {
//        getNow();
//        complete();
//        testJoin();
//        completeExceptionally();
//        testObtrudeException();


        /**
         * 可以直接获取第一阶段数据，同时不影响第二阶段数据处理
         */
        final CompletableFuture<String> future = errorHandle();
        future.whenComplete((v, t) -> System.out.println(v));




        Thread.currentThread().join();


    }


    private static CompletableFuture<String> errorHandle() {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            sleep(2);
            int i =1/0;
            System.out.println("======== i will be still process...");
            return "HELLO";
        });

        future.thenApply(s -> {
            Integer.parseInt(s);
            System.out.println("======== keep move");
            return s + " WORLD";
        }).exceptionally(Throwable::getMessage).thenAccept(System.out::println);

        return future;
    }


    /**
     * 强制导致后续调用方法{@link. #get（）}和相关方法抛出给定的异常，
     * 无论是否已经完成。此方法仅用于错误恢复操作，即使在这种情况下，也可能导致使用已建立与已覆盖结果的持续依赖完成。
     * <p>
     * 无论执行完与否，全部异常
     *
     * @throws NullPointerException if the exception is null
     * @param. ex the exception
     */
    private static void testObtrudeException() throws ExecutionException, InterruptedException {
//        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
//            System.out.println("======== i will be still process...");
//            sleep(2);
//            return "HELLO";
//        });
//
//        future.obtrudeException(new RuntimeException());
//        System.out.println(future.get());


        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            System.out.println("======== i will be still process...");
            return "HELLO";
        });
        sleep(1);
        future.obtrudeException(new RuntimeException());
        System.out.println(future.get());
    }


    /**
     * 如果尚未完成，则调用{@link. #get（）}和相关方法的调用以抛出给定的异常。
     * <p>
     * 和complete一样，不过是异常处理
     *
     * @return {@code true} 如果此调用导致此CompletableFuture转换为已完成状态，则为{@code false}
     * @param. ex the exception
     */
    private static void completeExceptionally() throws ExecutionException, InterruptedException {
//        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
//            System.out.println("======== i will be still process...");
//            sleep(2);
//            return "HELLO";
//        });
//
//        future.completeExceptionally(new RuntimeException());
//        System.out.println(future.get());

//        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
//            System.out.println("======== i will be still process...");
//            return "HELLO";
//        });
//        sleep(1);
//        future.completeExceptionally(new RuntimeException());
//        System.out.println(future.get());


        /**
         * 异常不处理就中断喽
         */
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            sleep(2);
            System.out.println("======== i will be still process...");
            return "HELLO";
        });
        sleep(1);
        future.completeExceptionally(new RuntimeException());
        System.out.println(future.get());
    }


    /**
     * 完成后返回结果值，如果异常完成则抛出（未经检查的）异常。为了更好地符合常用函数形式的使用，
     * 如果完成此CompletableFuture所涉及的计算引发异常，
     * 则此方法抛出（未经检查的）{CompletionException}，并将基础异常作为其原因。
     *
     * @return the result value
     * @throws. CancellationException 如果计算被取消
     * @throws. CompletionException 如果这个未来完成了
     * exceptionally or a completion computation threw an exception
     */
    private static void testJoin() {
        final CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            sleep(2);
            return "HELLO";
        });

        final String result = future.join();
        System.out.println(result);
    }


    /**
     * 如果尚未完成，请设置{@link返回的值#get（）}和给定值的相关方法。
     * <p>
     * 执行时get不会阻塞，因为没完成返回true同时返回设定值(注意，future没有执行，则原方法不会继续执行)
     *
     * @return {@code true} 如果此调用导致此CompletableFuture
     * 转换到已完成状态，否则{@code false}
     */
    private static void complete() throws ExecutionException, InterruptedException {
        final CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            sleep(2);
            System.out.println("======== i will be still process...");
            return "HELLO";
        });

        final boolean finished = future.complete("WORLD");

        System.out.println(finished);
        System.out.println(future.get());


//        final CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
//            //sleep(5);
//            System.out.println("======== i will be still process...");
//            return "HELLO";
//        });
//
//        sleep(1);
//        final boolean finished = future.complete("WORLD");
//
//        System.out.println(finished);
//        System.out.println(future.get());


//        final CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
//            sleep(2);
//            System.out.println("======== i will be still process...");
//            return "HELLO";
//        });
//
//        sleep(1);
//        final boolean finished = future.complete("WORLD");
//
//        System.out.println(finished);
//        System.out.println(future.get());


    }


    /**
     * 级联结束返回
     *
     * @throws ExecutionException
     * @throws InterruptedException
     */
    private static void getNow() throws ExecutionException, InterruptedException {
//        final String world = CompletableFuture.supplyAsync(() -> {
//            sleep(5);
//            return "HELLO";
//        }).getNow("WORLD");


        final CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            sleep(5);
            return "HELLO";
        });

        //一旦得到，则证明级联结束
        String result = future.getNow("WORLD");

        System.out.println(result);
        System.out.println(future.get());
    }


    /**
     * sleep
     *
     * @param seconds
     */
    private static void sleep(long seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
