package com.n33.jcu.executors.completablefuture;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * 组合方法
 *
 * @author N33
 * @date 2019/6/5
 */
public class CompletableFutureExample4 {

    public static void main(String[] args) throws InterruptedException {
//        thenAcceptBoth();

//        acceptEither();

//        runAfterBoth();

//        runAfterEither();

//        combine();

        compose();

        Thread.currentThread().join();


    }


    /**
     * 前者输出做后者输入
     */
    private static void compose() {
        CompletableFuture.supplyAsync(() -> {
            System.out.println("start the compose1");
            sleep(2);
            System.out.println("end the compose1");
            return "compose-1";
        }).thenCompose(s -> CompletableFuture.supplyAsync(() -> {
            System.out.println("start the compose2");
            sleep(2);
            System.out.println("end the compose2");
            return s.length();
        })).thenAccept(System.out::println);
    }


    /**
     * 合并组合,加工结果继续处理
     * 与acceptBoth区别是返回值和Void
     */
    private static void combine() {
        CompletableFuture.supplyAsync(() -> {
            System.out.println("start the combine1");
            sleep(2);
            System.out.println("end the combine1");
            return "combine-1";
        }).thenCombine(   CompletableFuture.supplyAsync(() -> {
            System.out.println("start the combine2");
            sleep(2);
            System.out.println("end the combine2");
            return 100;
        }),(s, i) -> s.length()>i).whenComplete((v, t) -> System.out.println(v));
    }







    /**
     * 只关心一个
     */
    private static void runAfterEither() {
        CompletableFuture.supplyAsync(() -> {
            System.out.println("start the runAfterEither1");
            sleep(2);
            System.out.println("end the runAfterEither1");
            return "runAfterEither-1";
        }).runAfterEitherAsync(CompletableFuture.supplyAsync(() -> {
            System.out.println("start the runAfterEither2");
            sleep(5);
            System.out.println("end the runAfterEither2");
            return "runAfterEither-1";
        }), () -> System.out.println("DONE"));
        System.out.println("=================");
    }



    /**
     * 在两个执行完以后执行
     * 无需返回值
     */
    private static void runAfterBoth() {
        CompletableFuture.supplyAsync(() -> {
            System.out.println("start the runAfterBoth");
            sleep(2);
            System.out.println("end the runAfterBoth");
            return "runAfterBoth-1";
        }).runAfterBothAsync(CompletableFuture.supplyAsync(() -> {
            System.out.println("start the runAfterBoth");
            sleep(5);
            System.out.println("end the runAfterBoth");
            return "runAfterBoth-1";
        }), () -> System.out.println("DONE"));
        System.out.println("=================");
    }



    /**
     * 类型一样
     * 执行先输出的那个
     * 执行完一个回调
     */
    private static void acceptEither() {

        CompletableFuture.supplyAsync(() -> {
            System.out.println("start the acceptEither1");
            sleep(2);
            System.out.println("end the acceptEither1");
            return "acceptEither-1";
        }).acceptEither(CompletableFuture.supplyAsync(() -> {
            System.out.println("start the acceptEither2");
            sleep(5);
            System.out.println("end the acceptEither2");
            return "acceptEither-1";
        }), System.out::println);



    }







    /**
     * 同时运行两个返回不同值的future
     * 都要执行完才执行回调
     */
    private static void thenAcceptBoth() {
        CompletableFuture.supplyAsync(() -> {
            System.out.println("start the supplyAsync");
            sleep(2);
            System.out.println("end the supplyAsync");
            return "thenAcceptBoth";
        }).thenAcceptBoth(CompletableFuture.supplyAsync(() -> {
            System.out.println("start the thenAcceptBoth");
            sleep(5);
            System.out.println("end the theAcceptBoth");
            return 100;
        }),(s, i) -> System.out.println(s+"========="+i));

    }




    /**
     * sleep
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
