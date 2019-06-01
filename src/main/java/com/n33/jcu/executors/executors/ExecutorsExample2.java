package com.n33.jcu.executors.executors;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Executors.newWorkStealingPool()
 * CPU线程
 *
 * @author N33
 * @date 2019/5/29
 */
public class ExecutorsExample2 {
    public static void main(String[] args) throws InterruptedException {
//        Optional.of(Runtime.getRuntime().availableProcessors()).ifPresent(System.out::println);

        ExecutorService executorService = Executors.newWorkStealingPool();


        final List<Callable<String>> callableList = IntStream.range(0, 20).boxed().map(i ->
                (Callable<String>) () -> {
                    System.out.println("Thread " + Thread.currentThread().getName());
                    sleep(2);
                    return "Task-" + i;
                }).collect(Collectors.toList());

        executorService.invokeAll(callableList).stream().map(future->{
            try {
                return future.get();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).forEach(System.out::println);



    }

    private static void sleep(long seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


}
/**
 * 使用所有{@link Runtime＃availableProcessors可用处理器}作为其目标并行度级别创建工作窃取线程池。
 *
 * @return the newly created thread pool
 * @see #newWorkStealingPool(int)
 * @since 1.8
 * <p>
 * <p>
 * 几个cpu核心，几个线程，完美利用核心
 * <pre>
 *   public static ExecutorService newWorkStealingPool() {
 *         return new ForkJoinPool
 *                 (Runtime.getRuntime().availableProcessors(),
 *                         ForkJoinPool.defaultForkJoinWorkerThreadFactory,
 *                         null, true);
 *     }
 * </pre>
 */
