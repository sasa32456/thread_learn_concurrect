package com.n33.jcu.executors.scheduledexecutorservice;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 定时任务
 *
 * @author N33
 * @date 2019/6/3
 */
public class ScheduledExecutorServiceExample1 {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        final ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(2);

//        final ScheduledFuture<?> future = executor.schedule(() -> System.out.println("=========  I will be invoked!"), 2, TimeUnit.SECONDS);
//        System.out.println(future.cancel(true));


//        final ScheduledFuture<Integer> future = executor.schedule(() -> 2, 2, TimeUnit.SECONDS);
//        System.out.println(future.get());


        /**
         *
         * 一秒后执行，每两秒执行一次
         */
//        executor.scheduleAtFixedRate(() ->System.out.println("I am running "  + System.currentTimeMillis()), 1, 2, TimeUnit.SECONDS);


        /**
         * 周期策略：是执行完才执行下个，还是周期至上，开新线程
         *
         * 和Timer一样，任务完成执行下个
         */
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
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        }, 1, 2, TimeUnit.SECONDS);


    }
}
/**
 * 一个{@link ExecutorService}，它可以调度命令在给定的延迟后运行，或者定期执行。
 *
 * <p> {@code schedule}方法创建具有各种延迟的任务，并返回可用于取消或检查执行的任务对象。
 * {@code scheduleAtFixedRate}和{@code scheduleWithFixedDelay}方法创建并执行定期运行的任务，直到被取消。
 *
 * <p>使用{@link Executor＃execute（Runnable）}和{@link ExecutorService} {@code submit}方法提交的命令的调度请求延迟为零。
 * {@code schedule}方法中也允许零延迟和负延迟（但不包括句点），并将其视为立即执行的请求。
 *
 * <p>所有{@code schedule}方法都接受<em>相对</ em>延迟和句点作为参数，而不是绝对时间或日期。
 * 将表示为{@link java.util.Date}的绝对时间转换为所需的形式是一件简单的事情。
 * 例如，要在某个将来安排{@code date}，您可以使用：
 * {@code schedule（task，date.getTime（） -  System.currentTimeMillis（），TimeUnit.MILLISECONDS）}。
 * 但请注意，相对延迟的到期不需要与由于网络时间同步协议，时钟漂移或其他因素而启用任务的当前{@code Date}一致。
 *
 * <p> {@link Executors}类为此程序包中提供的ScheduledExecutorService实现提供了方便的工厂方法。
 *
 * <h3>Usage Example</h3>
 *
 * Here is a class with a method that sets up a ScheduledExecutorService to beep every ten seconds for an hour:
 * 这是一个带有方法的类，该方法将ScheduledExecutorService设置为每隔十秒钟发出一小时的哔声：
 *
 *  <pre> {@code
 * import static java.util.concurrent.TimeUnit.*;
 * class BeeperControl {
 *   private final ScheduledExecutorService scheduler =
 *     Executors.newScheduledThreadPool(1);
 *
 *   public void beepForAnHour() {
 *     final Runnable beeper = new Runnable() {
 *       public void run() { System.out.println("beep"); }
 *     };
 *     final ScheduledFuture<?> beeperHandle =
 *       scheduler.scheduleAtFixedRate(beeper, 10, 10, SECONDS);
 *     scheduler.schedule(new Runnable() {
 *       public void run() { beeperHandle.cancel(true); }
 *     }, 60 * 60, SECONDS);
 *   }
 * }}</pre>
 *
 * @since 1.5
 * @author Doug Lea
 * public interface ScheduledExecutorService extends ExecutorService {}
 *
 *
 *
 * 一个{@link ThreadPoolExecutor}，可以额外安排命令在给定的延迟后运行，或定期执行。
 * 当需要多个工作线程时，或者当需要{@link ThreadPoolExecutor}（此类扩展）
 * 的额外灵活性或功能时，此类优于{@link java.util.Timer}。
 *
 * <p>延迟任务在启用后立即执行，但没有任何实时保证，启用它们后何时启动它们。
 * 按照先进先出（FIFO）提交顺序启用计划完全相同执行时间的任务。
 *
 * <p>在提交的任务在运行之前被取消时，将禁止执行。默认情况下，此类已取消的任务不会自动从工作队列中删除，
 * 直到其延迟结束。虽然这可以进一步检查和监控，但也可能导致取消任务的无限制保留。
 * 要避免这种情况，请将{@link #setRemoveOnCancelPolicy}设置为{@code true}，这
 * 会导致在取消时立即从工作队列中删除任务。
 *
 * <p>通过{@code scheduleAtFixedRate}或{@code scheduleWithFixedDelay}安排的任务的连续执行不会重叠。
 * 虽然不同的线程可以执行不同的执行，但先前执行的效果<a href="package-summary.html#MemoryVisibility">
 *     <i>发生在之后的</ i> </a>之后。
 *
 * <p>虽然这个类继承自{@link ThreadPoolExecutor}，但是一些继承的调优方法对它没用。
 * 特别是，因为它使用{@code corePoolSize}线程和无界队列作为固定大小的池，
 * 所以对{@code maximumPoolSize}的调整没有任何有用的效果。此外，
 * 将{@code corePoolSize}设置为零或使用{@code allowCoreThreadTimeOut}几乎绝不是一个好主意，
 * 因为一旦它们有资格运行，这可能会使池没有线程来处理任务。
 *
 * <p> <b>扩展注释：</ b>该类重写{@link ThreadPoolExecutor＃execute（Runnable）execute}
 * 和{@link AbstractExecutorService＃submit（Runnable）submit}方法以生成内部
 * {@link ScheduledFuture}对象控制每个任务的延迟和调度。为了保留功能，
 * 子类中这些方法的任何进一步覆盖必须调用超类版本，这有效地禁用了其他任务自定义。
 * 但是，此类提供了替代的受保护扩展方法{@code decorateTask}（{@code Runnable}和{@code Callable}各一个版本），
 * 可用于自定义用于执行通过{@code输入的命令的具体任务类型执行}，{@code submit}，{@code schedule}，
 * {@code scheduleAtFixedRate}和{@code scheduleWithFixedDelay}。默认情况下，
 * {@code ScheduledThreadPoolExecutor}使用扩展{@link FutureTask}的任务类型。
 * 但是，可以使用以下形式的子类来修改或替换它：
 *
 *  <pre> {@code
 * public class CustomScheduledExecutor extends ScheduledThreadPoolExecutor {
 *
 *   static class CustomTask<V> implements RunnableScheduledFuture<V> { ... }
 *
 *   protected <V> RunnableScheduledFuture<V> decorateTask(
 *                Runnable r, RunnableScheduledFuture<V> task) {
 *       return new CustomTask<V>(r, task);
 *   }
 *
 *   protected <V> RunnableScheduledFuture<V> decorateTask(
 *                Callable<V> c, RunnableScheduledFuture<V> task) {
 *       return new CustomTask<V>(c, task);
 *   }
 *   // ... add constructors, etc.
 * }}</pre>
 *
 * @since 1.5
 * @author Doug Lea
 *
 * public class ScheduledThreadPoolExecutor
 *         extends ThreadPoolExecutor
 *         implements ScheduledExecutorService {}
 */
