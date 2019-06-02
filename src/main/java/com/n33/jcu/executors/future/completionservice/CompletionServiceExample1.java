package com.n33.jcu.executors.future.completionservice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

/**
 * future 缺陷
 *
 * @author N33
 * @date 2019/6/2
 */
public class CompletionServiceExample1 {


    public static void main(String[] args) throws Exception {


//        futureDefect1();
        futureDefect2();

    }

    /**
     * future在1.8之前不能callback
     * get会发生blocked
     */
    private static void futureDefect1() throws ExecutionException, InterruptedException {
        final ExecutorService executorService = Executors.newFixedThreadPool(2);

        final Future<Integer> future = executorService.submit(() -> {
            try {
                TimeUnit.SECONDS.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 10;
        });

        System.out.println("==============");
        future.get();
    }


    /**
     * 无法拿到最先执行玩的结果,浪费性能
     * @throws InterruptedException
     * @throws ExecutionException
     */
    private static void futureDefect2() throws InterruptedException, ExecutionException {
        final ExecutorService executorService = Executors.newFixedThreadPool(2);

        final List<Callable<Integer>> callableList = Arrays.asList(
                () -> {
                    sleep(2);
                    System.out.println("The 2 finished.");
                    return 2;
                },
                () -> {
                    sleep(3);
                    System.out.println("The 3 finished.");
                    return 3;
                }
        );

//        final List<Future<Integer>> futures = executorService.invokeAll(callableList);

        List<Future<Integer>> futures = new ArrayList<>();
        futures.add(executorService.submit(callableList.get(0)));
        futures.add(executorService.submit(callableList.get(1)));

//        final Integer v1 = futures.get(1).get();
//        System.out.println(v1);
//
//        final Integer v2 = futures.get(0).get();
//        System.out.println(v2);

        for (Future<Integer> future : futures) {
            System.out.println(future.get());
        }

    }


    /**
     * sleep the specify seconds
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
/**
 * 一种服务，它将新异步任务的生成与已完成任务的结果消耗分离。
 * 生产者{@code submit}任务执行。消费者{@code take}完成任务并按照他们完成的顺序处理结果。
 * 例如，可以使用{@code CompletionService}来管理异步I / O，其中执行读取的任务在程序或系统的一个部分中提交，
 * 然后在读取完成时在程序的不同部分中执行操作。 ，可能的顺序与他们要求的顺序不同。
 *
 * <p>通常，{@code CompletionService}依赖于单独的{@link Executor}来实际执行任务，
 * 在这种情况下，{@code CompletionService}仅管理内部完成队列。
 * {@link ExecutorCompletionService}类提供了此方法的实现。
 *
 * <p>内存一致性效果：在将任务提交到{@code CompletionService}
 * 之前线程中的操作<a href="package-summary.html#MemoryVisibility">
 * <i>发生在</ i>之前</ a >该任务采取的行动，
 * 在相应的{@code take（）}成功返回之后，<i>发生在</ i>之前的行动。
 * public interface CompletionService<V> {}
 */
