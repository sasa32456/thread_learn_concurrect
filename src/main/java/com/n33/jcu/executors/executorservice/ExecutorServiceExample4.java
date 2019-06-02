package com.n33.jcu.executors.executorservice;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 深入线程服务invokeAll
 *
 * @author N33
 * @date 2019/6/2
 */
public class ExecutorServiceExample4 {

    public static void main(String[] args) throws ExecutionException, InterruptedException, TimeoutException {
//        testInvokeAny();

//        testInvokeAnyTimeOut();

//        testInvokeAll();

//        testInvokeAllTimeOut();

//        testSubmitRunnable();

        testSubmitRunnableWithResult();

    }


    /**
     * Question:
     * when the result returned, other callable will be keep on process?
     * <p>
     * Answer:
     * Pther callable will be canceled.(if other not process finished)
     * <p>
     * Note:
     * The invokeAny will be blocked caller.
     * <p>
     * {@link ExecutorService#invokeAll(Collection)}
     */
    private static void testInvokeAny() throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        final List<Callable<Integer>> callableList = IntStream.range(0, 5).boxed().map(i -> (Callable<Integer>) () -> {
            TimeUnit.SECONDS.sleep(ThreadLocalRandom.current().nextInt(3));
            System.out.println(Thread.currentThread().getName() + " : " + i);
            return i;
        }).collect(Collectors.toList());


        //ExecutionException执行异常
        //InterruptedException打断异常，方法是同步方法
        Integer value = executorService.invokeAny(callableList);

        System.out.println("=========finished============");
        System.out.println(value);
    }

    /**
     * Exception in thread "main" java.util.concurrent.TimeoutException
     * {@link ExecutorService#invokeAny(Collection, long, TimeUnit)}}
     */
    private static void testInvokeAnyTimeOut() throws ExecutionException, InterruptedException, TimeoutException {
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        Integer value = executorService.invokeAny(IntStream.range(0, 5).boxed().map(i -> (Callable<Integer>) () -> {
            TimeUnit.SECONDS.sleep(ThreadLocalRandom.current().nextInt(10));
            System.out.println(Thread.currentThread().getName() + " : " + i);
            return i;
        }).collect(Collectors.toList()), 1, TimeUnit.SECONDS);


        System.out.println("=========finished============");
        System.out.println(value);
    }


    /**
     * 执行所有,blocked
     * <p>
     * RxJava
     * JDK9
     * 支持执行完继续下个任务
     * <p>
     * <p>
     * {@link ExecutorService#invokeAll(Collection)}
     */
    private static void testInvokeAll() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        /**
         * 一批批执行，执行中不等待，执行结束等待,废话，但是执行结束无法执行下个任务，必须一起执行
         */
        executorService.invokeAll(IntStream.range(0, 5).boxed().map(i -> (Callable<Integer>) () -> {
            TimeUnit.SECONDS.sleep(ThreadLocalRandom.current().nextInt(10));
            System.out.println(Thread.currentThread().getName() + " : " + i);
            return i;
        }).collect(Collectors.toList())).parallelStream().map(future -> {
            try {
                return future.get();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).forEach(System.out::println);


        System.out.println("=========finished============");
    }


    /**
     * Exception in thread "main" java.lang.RuntimeException: java.util.concurrent.CancellationException
     *
     * @throws InterruptedException
     */
    private static void testInvokeAllTimeOut() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        executorService.invokeAll(IntStream.range(0, 5).boxed().map(i -> (Callable<Integer>) () -> {
            TimeUnit.SECONDS.sleep(ThreadLocalRandom.current().nextInt(10));
            System.out.println(Thread.currentThread().getName() + " : " + i);
            return i;
        }).collect(Collectors.toList()), 1, TimeUnit.SECONDS).parallelStream().map(future -> {
            try {
                return future.get();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).forEach(System.out::println);


        System.out.println("=========finished============");
    }


    /**
     * {@link ExecutorService#submit(Runnable)}
     */
    private static void testSubmitRunnable() throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(10);


        Future<?> future = executorService.submit(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        //Blocked
        Object NULL = future.get();

        System.out.println("R: " + NULL);

    }




    /**
     * {@link ExecutorService#submit(Runnable, Object)}
     */
    private static void testSubmitRunnableWithResult() throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        String result = "DONE";

        final Future<String> future = executorService.submit(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, result);

        System.out.println(future.get());

    }





}
/**
 * 返回结果并可能抛出异常的任务。实现者定义一个没有名为{@code call}的参数的方法。
 *
 * <p> {@code Callable}接口类似于{@link java.lang.Runnable}，
 * 因为它们都是为其实例可能由另一个线程执行的类而设计的。
 * 但是，{@code Runnable}不返回结果，也不能抛出已检查的异常。
 *
 * <p> {@link Executors}类包含将其他常用表单转换为{@code Callable}类的实用程序方法。
 *
 * @author Doug Lea
 * @param <V> the result type of method {@code call}
 *
 * <pre>
 * @FunctionalInterface
 * public interface Callable<V> {
 *
 *     计算结果，或者如果无法执行则抛出异常。
 *
 *     @return computed result
 *     @throws Exception if unable to compute a result
 *
 *     V call() throws Exception;
 * }
 * </pre>
 * <p>
 * <p>
 * 执行给定的任务，返回完成所有状态和结果的Futures列表。
 * {@link Future＃isDone}对于返回列表的每个元素都是{@code true}。
 * 请注意，<em>已完成的</ em>任务可能已正常终止或通过抛出异常终止。
 * 如果在此操作正在进行时修改了给定集合，则此方法的结果是不确定的。
 * @param tasks 任务集合
 * @param <T> 从任务返回的值的类型
 * @return 表示任务的Futures列表，与迭代器为给定任务列表生成的顺序相同，每个已完成
 * @throws InterruptedException 如果在等待时被中断，在这种情况下，未完成的任务被取消
 * @throws NullPointerException 如果任务或其任何元素是{@code null}
 * @throws RejectedExecutionException 如果任何任务无法安排执行
 * <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks)
 * throws InterruptedException;
 * <p>
 * <p>
 * <p>
 * <p>
 * 提交Runnable任务以执行并返回表示该任务的Future。
 * <em>成功</ em>完成后，Future的{@code get}方法将返回{@code null}。
 * @param task the task to submit
 * @return 表示未完成任务的Future
 * @throws RejectedExecutionException if the task cannot be
 * scheduled for execution
 * @throws NullPointerException if the task is null
 * Future<?> submit(Runnable task);
 */
