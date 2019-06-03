package com.n33.jcu.executors.scheduledexecutorservice;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * scheduleWithFixedDelay固定循环，执行后依旧再加上循环时间
 *
 * @author N33
 * @date 2019/6/3
 */
public class ScheduledExecutorServiceExample2 {
    public static void main(String[] args) throws Exception{

//        testScheduleWithFixedDelay();
//        test1();
        test2();

    }

    private static void testScheduleWithFixedDelay() {
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(2);

//        executor.scheduleWithFixedDelay(() -> System.out.println("===" + System.currentTimeMillis()), 1, 2, TimeUnit.SECONDS);

        final AtomicLong interval = new AtomicLong(0L);

        final ScheduledFuture<?> scheduledFuture = executor.scheduleWithFixedDelay(() -> {
            final long currentTimeMillis = System.currentTimeMillis();
            if (interval.get() == 0) {
                System.out.printf("The first time trigger task at %d \n", currentTimeMillis);
                interval.set(currentTimeMillis);
            } else {
                System.out.printf("The actually spend [%d]\n", currentTimeMillis - interval.get());
            }

            interval.set(currentTimeMillis);
            System.out.println(Thread.currentThread().getName());


            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        }, 1, 2, TimeUnit.SECONDS);
    }








    /**
     * getContinueExistingPeriodicTasksAfterShutdownPolicy
     * 获取有关是否继续执行现有周期性任务的策略，即使此执行程序已经{@code shutdown}。
     * 在这种情况下，这些任务仅在{@code shutdownNow}或在已经关闭时将策略设置为{@code false}后终止。
     * 默认情况下，此值为{@code false}。
     *
     *
     * 设置是否继续执行现有周期性任务的策略，即使此执行程序已经{@code shutdown}。
     * 在这种情况下，这些任务仅在{@code shutdownNow}或在已经关闭时将策略设置为{@code false}后终止。
     * 默认情况下，此值为{@code false}。
     */
    private static void test1() throws InterruptedException {
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(2);
        System.out.println(executor.getContinueExistingPeriodicTasksAfterShutdownPolicy());
        //设置即使shutdown也能继续执行
        executor.setContinueExistingPeriodicTasksAfterShutdownPolicy(true);

        final AtomicLong interval = new AtomicLong(0L);

        final ScheduledFuture<?> scheduledFuture = executor.scheduleAtFixedRate(() -> {
            final long currentTimeMillis = System.currentTimeMillis();
            if (interval.get() == 0) {
                System.out.printf("The first time trigger task at %d\n", currentTimeMillis);
                interval.set(currentTimeMillis);
            } else {
                System.out.printf("The actually spend [%d]\n", currentTimeMillis - interval.get());
            }

            interval.set(currentTimeMillis);
            System.out.println(Thread.currentThread().getName());

            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, 1, 2, TimeUnit.SECONDS);

        TimeUnit.MILLISECONDS.sleep(1200);
        executor.shutdown();

    }


    /**
     * getExecuteExistingDelayedTasksAfterShutdownPolicy
     * 获取有关是否执行现有延迟任务的策略，即使此执行程序已经{@code shutdown}。
     * 在这种情况下，这些任务仅在{@code shutdownNow}时终止，或者在已经关闭时将策略设置为{@code false}之后终止。
     * 默认情况下，此值为{@code true}。
     * 执行结束便结束
     *
     *
     * 设置是否执行现有延迟任务的策略，即使此执行程序已经{@code shutdown}。
     * 在这种情况下，这些任务仅在{@code shutdownNow}时终止，或者在已经关闭时将策略设置为{@code false}之后终止。
     * 默认情况下，此值为{@code true}。
     * 结束就不执行下去了
     *
     */
    private static void test2() throws InterruptedException {
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(2);
        System.out.println(executor.getExecuteExistingDelayedTasksAfterShutdownPolicy());
        executor.setExecuteExistingDelayedTasksAfterShutdownPolicy(false);

        final AtomicLong interval = new AtomicLong(0L);

        final ScheduledFuture<?> scheduledFuture = executor.scheduleWithFixedDelay(() -> {
            final long currentTimeMillis = System.currentTimeMillis();
            if (interval.get() == 0) {
                System.out.printf("The first time trigger task at %d\n", currentTimeMillis);
                interval.set(currentTimeMillis);
            } else {
                System.out.printf("The actually spend [%d]\n", currentTimeMillis - interval.get());
            }

            interval.set(currentTimeMillis);
            System.out.println(Thread.currentThread().getName());

            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, 1, 2, TimeUnit.SECONDS);

        TimeUnit.MILLISECONDS.sleep(1200);
        executor.shutdown();
        System.out.println("===over===");

    }




}
    /**
     * 创建并执行一个周期性动作，该动作在给定的初始延迟之后首先被启用，
     * 并且随后在一次执行的终止和下一次执行的开始之间给定延迟。
     * 如果任务的任何执行遇到异常，则后续执行被禁止。
     * 否则，任务将仅通过取消或终止执行者来终止。
     *
     * @param command the task to execute
     * @param initialDelay the time to delay first execution
     * @param 延迟一次执行终止和下一次执行开始之间的延迟
     * @param 单位initialDelay和delay参数的时间单位
     * @return ScheduledFuture表示任务的挂起完成，其{@code get（）}方法将在取消时抛出异常
     * @throws RejectedExecutionException if the task cannot be
     *         scheduled for execution
     * @throws NullPointerException if command is null
     * @throws IllegalArgumentException if delay less than or equal to zero
     *
     *
     *
     * public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command,
     *                                                      long initialDelay,
     *                                                      long delay,
     *                                                      TimeUnit unit);
     */
