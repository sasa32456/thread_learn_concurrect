package com.n33.jcu.executors.executors;

import java.util.concurrent.*;
import java.util.stream.IntStream;

/**
 * 3大自动创建线程池
 * @author N33
 * @date 2019/5/29
 */
public class ExecutorsExample1 {
    public static void main(String[] args) throws InterruptedException {
//        useCachedThreadPool();
//        useFixedSizePool();
        useSinglePool();

    }








    /**
     * 创建一个Executor，它使用一个在无界队列中运行的工作线程。
     * （但请注意，如果此单个线程由于在关闭之前执行期间的故障而终止，则在需要执行后续任务时将使用新的线程。）
     * 保证任务按顺序执行，并且不会有多个任务处于活动状态在任何给定的时间。
     * 与其他等效的{@code newFixedThreadPool（1）}不同，保证返回的执行程序不可重新配置以使用其他线程。
     *
     * @return the newly created single-threaded Executor
     *
     * public static ExecutorService newSingleThreadExecutor() {
     *         return new FinalizableDelegatedExecutorService
     *                 (new ThreadPoolExecutor(1, 1,
     *                         0L, TimeUnit.MILLISECONDS,
     *                         new LinkedBlockingQueue<Runnable>()));
     *
     *
     *  和 new Thread 的区别
     *  1.Thread will die after finish work , but singleThreadExecutor can always alive
     *  2.Thread can not put the submitted runnable to the cache queue but SingleThreadExecutor can do this
     */
    private static void useSinglePool() throws InterruptedException {
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        /**
         * Exception in thread "main" java.lang.ClassCastException: java.util.concurrent.Executors$FinalizableDelegatedExecutorService
         * cannot be cast to java.util.concurrent.ThreadPoolExecutor
         * 类型转换失败
         */
//        System.out.println(((ThreadPoolExecutor)executorService).getActiveCount());

        IntStream.range(0, 100).forEach(i -> executorService.execute(() -> {
            try {
                TimeUnit.MILLISECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() +" [" + i +"] ");
        }));

        TimeUnit.SECONDS.sleep(1);
//        System.out.println(((ThreadPoolExecutor)executorService).getActiveCount());


    }



    /**
     * 创建一个线程池，该线程池重用在共享的无界队列中运行的固定数量的线程。
     * 在任何时候，最多{@code nThreads}线程将是活动的处理任务。
     * 如果在所有线程都处于活动状态时提交了其他任务，则它们将在队列中等待，
     * 直到线程可用。如果任何线程由于在关闭之前执行期间的故障而终止，
     * 则在需要执行后续任务时将使用新的线程。池中的线程将一直存在，
     * 直到它显式{@link ExecutorService #shutdown shutdown}。
     *
     * @param -nTthreads池中的线程数
     * @return 新创建的线程池
     * @throws IllegalArgumentException if {@code nThreads <= 0}
     *
     *     public static ExecutorService newFixedThreadPool(int nThreads) {
     *         return new ThreadPoolExecutor(nThreads, nThreads,
     *                 0L, TimeUnit.MILLISECONDS,
     *                 new LinkedBlockingQueue<Runnable>());
     *
     *
     * 基于链接节点的可选绑定{@linkplain BlockingQueue blocking queue}。
     * 此队列对元素FIFO（先进先出）进行排序。队列的<em> head </ em>是队列中最长时间的元素。
     * 队列的<em> tail </ em>是队列中最短时间的元素。新元素插入队列的尾部，
     * 队列检索操作获取队列头部的元素。链接队列通常具有比基于阵列的队列更高的吞吐量，
     * 但在大多数并发应用程序中具有较低的可预测性能。
     *
     * <p>可选的容量绑定构造函数参数用作防止过多队列扩展的方法。如果未指定，
     * 则容量等于{@link Integer #MAX_VALUE}。每次插入时都会动态创建链接节点，除非这会使队列超出容量。
     *
     *     public class LinkedBlockingQueue<E> extends AbstractQueue<E>
     *             implements BlockingQueue<E>, java.io.Serializable {
     */
    private static void useFixedSizePool() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        System.out.println(((ThreadPoolExecutor)executorService).getActiveCount());
        executorService.execute(() -> System.out.println("====================="));
        System.out.println(((ThreadPoolExecutor)executorService).getActiveCount());

        IntStream.range(0, 100).forEach(i -> executorService.execute(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() +" [" + i +"] ");
        }));

        TimeUnit.SECONDS.sleep(1);
        System.out.println(((ThreadPoolExecutor)executorService).getActiveCount());

    }

    /**
     * These pools will typically improve the performance
     * of programs that execute many short-lived asynchronous tasks.
     *
     * 只适合短处理
     *
     * public static ExecutorService newCachedThreadPool() {
     *         return new ThreadPoolExecutor(0, Integer.MAX_VALUE,
     *                                       60L, TimeUnit.SECONDS,
     *                                       new SynchronousQueue<Runnable>());
     */
    private static void useCachedThreadPool() throws InterruptedException {
        ExecutorService executorService = Executors.newCachedThreadPool();

        System.out.println(((ThreadPoolExecutor)executorService).getActiveCount());
        /**
         * 先放入queue，queue只存放一个，创建线程，移除queue
         *
         * queue只有取出才会放入
         */
        executorService.execute(() -> System.out.println("====================="));
        System.out.println(((ThreadPoolExecutor)executorService).getActiveCount());

        IntStream.range(0, 100).forEach(i -> executorService.execute(() -> {
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() +" [" + i +"] ");
        }));

        TimeUnit.SECONDS.sleep(1);
        System.out.println(((ThreadPoolExecutor)executorService).getActiveCount());
    }


}
/**
 * 此程序包中定义的{@link Executor}，{@link ExecutorService}，
 * {@link ScheduledExecutorService}，{@link ThreadFactory}和
 * {@link Callable}类的工厂和实用程序方法。该类支持以下几种方法：
 *
 * <ul>
 * <li> 创建和返回{@link ExecutorService}的方法，使用常用的配置设置进行设置。
 * <li> 使用常用配置设置创建并返回{@link ScheduledExecutorService}的方法。
 * <li> 创建并返回“包装”ExecutorService的方法，通过使特定于实现的方法不可访问来禁用重新配置。
 * <li> 创建并返回{@link ThreadFactory}的方法，该方法将新创建的线程设置为已知状态。
 * <li> 从其他类似闭包形式创建并返回{@link Callable}的方法，因此可以在需要{@code Callable}的执行方法中使用它们。
 * </ul>
 *<pre>
 * @author Doug Lea
 * public class Executors {
 * <p>
 * <p>
 * 创建一个根据需要创建新线程的线程池，但在它们可用时将重用以前构造的线程。
 * 这些池通常会提高执行许多短期异步任务的程序的性能。
 * 对{@code execute}的调用将重用以前构造的线程（如果可用）。
 * 如果没有可用的现有线程，则将创建一个新线程并将其添加到池中。
 * 未使用60秒的线程将终止并从缓存中删除。因此，长时间闲置的池不会消耗任何资源。
 * 请注意，可以使用{@link ThreadPoolExecutor}构造函数创建具有相似属性但不同详细信息的池（例如，超时参数）。
 * @return新创建的线程池
 *
 *  public static ExecutorService newCachedThreadPool() {
 *      return new ThreadPoolExecutor(0, Integer.MAX_VALUE,
 *      60L, TimeUnit.SECONDS,new SynchronousQueue<Runnable>());
 *  }
 * }
 * </pre>
 * @since 1.5
 *
 *
 * 一个{@linkplain BlockingQueue阻塞队列}，其中每个插入操作必须等待另一个线程执行相应的删除操作，
 * 反之亦然。同步队列没有任何内部容量，甚至没有容量。你不能在同步队列中{@code peek}，
 * 因为只有当你试图删除它时才会出现一个元素;你不能插入一个元素（使用任何方法），
 * 除非另一个线程试图删除它;你不能迭代，因为没有什么可以迭代。
 * 队列的<em> head </ em>是第一个排队插入线程尝试添加到队列的元素;
 * 如果没有这样排队的线程，则没有可用于删除的元素，{@code poll（）}将返回{@code null}。
 * 出于其他{@code Collection}方法的目的（例如{@code contains}），
 * {@code SynchronousQueue}充当空集合。此队列不允许{@code null}元素。
 *
 * <p>同步队列类似于CSP和Ada中使用的集合点通道。它们非常适用于切换设计，
 * 其中在一个线程中运行的对象必须与在另一个线程中运行的对象同步，以便将其传递给某些信息，事件或任务。
 *
 * <p>此类支持用于排序等待生产者和消费者线程的可选公平策略。默认情况下，不保证此顺序。
 * 但是，使用设置为{@code true}的公平性构造的队列以FIFO顺序授予线程访问权限。
 *
 * <p>此类及其迭代器实现{@link Collection}和{@link Iterator}接口的所有<em>可选</ em>方法。
 *
 * <p>This class is a member of the
 * <a href="{@docRoot}/../technotes/guides/collections/index.html">
 * Java Collections Framework</a>.
 *
 * @since 1.5
 * @author Doug Lea and Bill Scherer and Michael Scott
 * @param <E> the type of elements held in this collection
 *
 *           public class SynchronousQueue<E> extends AbstractQueue<E>
 *         implements BlockingQueue<E>, java.io.Serializable {}
 */
