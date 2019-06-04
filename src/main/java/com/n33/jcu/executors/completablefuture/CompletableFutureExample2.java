package com.n33.jcu.executors.completablefuture;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * 构造方法
 * (尽量静态工厂)
 *
 * @author N33
 * @date 2019/6/4
 */
public class CompletableFutureExample2 {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
//        supplyAsync();

//        final Future<?> future = runAsync();
//        future.get();


//        final Future<Void> future = completed("Hello");
//        System.out.println(future.isDone());

//        System.out.println(">>>>>>>>>>>>>>"+anyOf().get());

        allOf();

        Thread.currentThread().join();
    }


    /**
     * 默认构造，建议不要用，除非。。。
     */
    private static void create() {
        final CompletableFuture<Object> future = new CompletableFuture<>();

        //等价于
        Object s = null;
        CompletableFuture.<Object>supplyAsync(() -> s);
    }







    /**
     * 全执行，无返回
     */
    private static void allOf() {

        CompletableFuture.allOf(CompletableFuture.runAsync(() -> {
                    try {
                        System.out.println("1======Start");
                        TimeUnit.SECONDS.sleep(2);
                        System.out.println("1======End");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }).whenComplete((aVoid, throwable) -> System.out.println("=========over==========")),
                CompletableFuture.supplyAsync(() -> {
                    try {
                        System.out.println("2======Start");
                        TimeUnit.SECONDS.sleep(1);
                        System.out.println("2======End");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return "HELLO";
                }).whenComplete((aVoid, throwable) -> System.out.println("=========over==========")));

    }





    /**
     * 全执行，返回任一
     */
    private static Future<?> anyOf() {
       return CompletableFuture.anyOf(CompletableFuture.runAsync(() -> {
            try {
                System.out.println("1======Start");
                TimeUnit.SECONDS.sleep(2);
                System.out.println("1======End");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).whenComplete((aVoid, throwable) -> System.out.println("=========over==========")),
           CompletableFuture.supplyAsync(() -> {
            try {
                System.out.println("2======Start");
                TimeUnit.SECONDS.sleep(3);
                System.out.println("2======End");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
               return "HELLO";
        }).whenComplete((aVoid, throwable) -> System.out.println("=========over==========")));

    }







    /**
     * 生成一个已完成的CompletableFuture
     * @param data
     * @return
     */
    private static Future<Void> completed(String data) {
        return CompletableFuture.completedFuture(data).thenAccept(System.out::println);
    }





    /**
     * runAsync 返回 CompletableFuture以便后续级联
     * @return
     */
    private static Future<?> runAsync() {

       return CompletableFuture.runAsync(() -> {
            try {
                System.out.println("Obj======Start");
                TimeUnit.SECONDS.sleep(2);
                System.out.println("Obj======End");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).whenComplete((aVoid, throwable) -> System.out.println("=========over=========="));


    }


    /**
     * <pre>
     * 场景
     * post->{
     *     basic
     *     detail
     * }
     * insert basic
     * insert detail
     *
     *            insert basic
     * [submit]                     ==>action
     *            insert detail
     *
     * </pre>
     */
    private static void supplyAsync() {
        CompletableFuture.supplyAsync(Object::new)
                .thenAcceptAsync(obj -> {
                    try {
                        System.out.println("Obj======Start");
                        TimeUnit.SECONDS.sleep(2);
                        System.out.println("Obj======" + obj);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                })
                //两个一起执行
                .runAfterBoth(CompletableFuture.supplyAsync(() -> "Hello")
                        .thenAcceptAsync(s -> {
                            try {
                                System.out.println("String======Start");
                                TimeUnit.SECONDS.sleep(2);
                                System.out.println("String======" + s);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }), () -> System.out.println("=====Finished====="));
    }
}
