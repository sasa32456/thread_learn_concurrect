package com.n33.jcu.executors.threadpool;

import java.util.concurrent.*;

/**
 * 线程池创建
 *
 * @author N33
 * @date 2019/5/28
 */
public class ThreadPoolExecutorBuild {


    public static void main(String[] args) {
        ThreadPoolExecutor executorService = (ThreadPoolExecutor) buildThreadPoolExecutor();

        int activeCount = -1;
        int queueSize = -1;

        while (true) {
            if (activeCount != executorService.getActiveCount() || queueSize != executorService.getQueue().size()) {
                System.out.println("ActiveCount: " + executorService.getActiveCount());
                System.out.println("CorePoolSize: "+executorService.getCorePoolSize());
                System.out.println("QueueSize: " + executorService.getQueue().size());
                System.out.println("MaximumPoolSize: "+executorService.getMaximumPoolSize());

                activeCount = executorService.getActiveCount();
                queueSize = executorService.getQueue().size();
                System.out.println("=======================================");
            }
        }
    }


    /**
     * Testing point.
     * <p>
     * 1.corePoolSize=1,MaxSize=2,blockingQueue is emply what will happen when three task
     * 2.corePoolSize=1,MaxSize=2,blockingQueue size = 5,what happen when submit 7 task
     * 3.corePoolSize=1,MaxSize=2,blockingQueue size = 5,what happen when submit 8 task
     *
     * </p>
     * <p>
     * <p>
     * int corePoolSize,
     * int maximumPoolSize,
     * long keepAliveTime,
     * TimeUnit unit,
     * BlockingQueue<Runnable> workQueue,
     * ThreadFactory threadFactory,
     * RejectedExecutionHandler handler
     */
    private static ExecutorService buildThreadPoolExecutor() {
        ExecutorService executorService =
                new ThreadPoolExecutor(1, 2, 30,
                        TimeUnit.SECONDS, new ArrayBlockingQueue<>(1), r -> new Thread(r),
                        /**
                         * 拒绝任务的处理程序抛出一个
                         * {@code RejectedExecutionException}.
                         *   public static class AbortPolicy implements RejectedExecutionHandler {}
                         */
                        new ThreadPoolExecutor.AbortPolicy());
        System.out.println("The ThreadPoolExecutor create done.");

//        executorService.execute(() -> sleepSeconds(100));
//        executorService.execute(() -> sleepSeconds(1));
//        executorService.execute(() -> sleepSeconds(5));
//        //Exception in thread "main" java.util.concurrent.RejectedExecutionException:拒绝
//        executorService.execute(() -> sleepSeconds(100));

        //回收多余线程
        executorService.execute(() -> sleepSeconds(100));
        executorService.execute(() -> sleepSeconds(10));
        executorService.execute(() -> sleepSeconds(10));


        return executorService;
    }

    private static void sleepSeconds(long seconds) {
        try {
            System.out.println("* " + Thread.currentThread().getName()+ " *");
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
/**
 * 一个{@link ExecutorService}，它使用可能的几个池化线程之一执行每个提交的任务，通常使用{@link Executors}工厂方法配置。
 *
 * <p>线程池解决了两个不同的问题：由于减少了每个任务的调用开销，
 * 它们通常在执行大量异步任务时提供更高的性能，并且它们提供了一种绑定和管理资源（包括执行时消耗的线程）的方法一系列任务。
 * 每个{@code ThreadPoolExecutor}还维护一些基本统计信息，例如已完成任务的数量。
 *
 * <p>为了在各种上下文中有用，这个类提供了许多可调参数和可扩展性钩子。
 * 但是，程序员应该使用更方便的{@link Executors}工厂方法{@link Executors＃newCachedThreadPool}
 * （无界线程池，自动线程回收），{@link Executors＃newFixedThreadPool}（固定大小的线程池）和
 * { @link Executors＃newSingleThreadExecutor}（单一后台线程），
 * 为最常见的使用场景预配置设置。否则，在手动配置和调整此类时，请使用以下指南：
 *
 * <dl>
 *
 * <dt>核心和最大池大小</dt>
 *
 * <dd> {@code ThreadPoolExecutor}将根据corePoolSize（参见{@link #getCorePoolSize}）
 * 和maximumPoolSize（参见{@link #getMaximumPoolSize}设置的界限自动调整池大小（请参阅{@link #getPoolSize}） ）。
 * <p>
 * 在方法{@link #execute（Runnable）}中提交新任务时，并且运行的线程少于corePoolSize线程，
 * 即使其他工作线程处于空闲状态，也会创建一个新线程来处理该请求。
 * 如果有多个corePoolSize但运行的maximumPoolSize线程少于maximumPoolSize，
 * 则只有在队列已满时才会创建新线程。通过设置corePoolSize和maximumPoolSize，
 * 可以创建固定大小的线程池。通过将maximumPoolSize设置为基本无限制的值（例如{@code Integer.MAX_VALUE}），
 * 您可以允许池容纳任意数量的并发任务。最典型的情况是，核心和最大池大小仅在构造时设置，
 * 但也可以使用{@link #setCorePoolSize}和{@link #setMaximumPoolSize}动态更改。 </ DD>
 *
 * <dt>按需建设</dt>
 *
 * <dd>默认情况下，即使核心线程最初只在新任务到达时创建并启动，
 * 但可以使用方法{@link #prestartCoreThread}或{@link #prestartAllCoreThreads}动态覆盖。
 * 如果使用非空队列构造池，则可能需要预启动线程。 </ DD>
 *
 * <dt>创建新线程</dt>
 *
 * <dd>使用{@link ThreadFactory}创建新线程。如果没有另外指定，
 * 则使用{@link Executors＃defaultThreadFactory}，它创建的线程都在同一个{@link ThreadGroup}中，
 * 并具有相同的{@code NORM_PRIORITY}优先级和非守护进程状态。
 * 通过提供不同的ThreadFactory，您可以更改线程的名称，线程组，优先级，守护程序状态等。
 * 如果{@code ThreadFactory}在通过从{@code newThread}返回null而无法创建线程时，
 * 执行程序将继续，但可能无法执行任何任务。线程应该拥有“modifyThread”{@code RuntimePermission}。
 * 如果使用池的工作线程或其他线程不具有此权限，则服务可能会降级：
 * 配置更改可能不会及时生效，并且关闭池可能保持可以终止但未完成的状态。 / DD>
 *
 * <dt>保持活力</dt>
 *
 * <dd>如果池当前具有多个corePoolSize线程，则多余线程如果空闲时间超过keepAliveTime，
 * 则将终止（请参阅{@link #getKeepAliveTime（TimeUnit）}）。
 * 这提供了一种在不主动使用池时减少资源消耗的方法。
 * 如果池稍后变得更活跃，则将构造新线程。
 * 也可以使用方法{@link #setKeepAliveTime（long，TimeUnit）}动态更改此参数。
 * 使用值{@code Long.MAX_VALUE} {@link TimeUnit＃NANOSECONDS}可以有效地禁止空闲线程在关闭之前终止。
 * 默认情况下，保持活动策略仅在有多个corePoolSize线程时才适用。
 * 但是方法{@link #allowCoreThreadTimeOut（boolean）}也可用于将此超时策略应用于核心线程，
 * 只要keepAliveTime值为非零。 </ DD>
 *
 * <dt>排队</dt>
 *
 * <dd>任何{@link BlockingQueue}都可用于转移和保留已提交的任务。此队列的使用与池大小调整交互：
 *
 * <ul>
 *
 * <li>如果运行的corePoolSize线程少于，则执行程序总是更喜欢添加新线程而不是排队。</ li>
 *
 * <li>如果corePoolSize或更多线程正在运行，则Executor总是更喜欢排队请求而不是添加新线程。</ li>
 *
 * <li>如果请求无法排队，则会创建一个新线程，除非这会超过maximumPoolSize，在这种情况下，该任务将被拒绝。</ li>
 *
 * </ul>
 * <p>
 * 排队有三种常规策略：
 * <ol>
 *
 * <li> <em>直接切换。</ em>工作队列的一个很好的默认选择是{@link SynchronousQueue}，
 * 它将任务交给线程而不另外保存它们。在这里，如果没有线程立即可用于运行它，
 * 则尝试对任务进行排队将失败，因此将构造新线程。
 * 此策略在处理可能具有内部依赖性的请求集时避免了锁定。
 * 直接切换通常需要无限制的maximumPoolSizes以避免拒绝新提交的任务。
 * 这反过来承认，当命令继续以比处理它们更快的速度到达时，无限制的线程增长的可能性。 </ LI>
 *
 * <li> <em>无界队列。</ em>使用无界队列（例如没有预定义容量的{@link LinkedBlockingQueue}）
 * 将导致新任务在所有corePoolSize线程忙时在队列中等待。
 * 因此，只会创建corePoolSize线程。 （并且maximumPoolSize的值因此没有任何影响。）
 * 当每个任务完全独立于其他任务时，这可能是适当的，因此任务不会影响彼此的执行;
 * 例如，在网页服务器中。虽然这种排队方式可以用于平滑请求的瞬时突发，
 * 但它承认当命令继续以比处理它们更快的速度到达时，无限制的工作队列增长的可能性。 </ LI>
 *
 * <li> <em>有界队列。</ em>有限队列（例如，{@link ArrayBlockingQueue}）
 * 与有限maximumPoolSizes一起使用时有助于防止资源耗尽，
 * 但调整和控制更加困难。队列大小和最大池大小可以相互交换：
 * 使用大型队列和小型池最小化CPU使用率，OS资源和上下文切换开销，
 * 但可能导致人为的低吞吐量。如果任务经常阻塞（例如，如果它们是I / O绑定的），
 * 系统可能能够为您提供比您允许的更多线程的时间。使用小队列通常需要更大的池大小，
 * 这会使CPU更加繁忙，但可能会遇到不可接受的调度开销，这也会降低吞吐量。 </ LI>
 *
 * </ol>
 *
 * </dd>
 *
 * <dt>被拒绝的任务</dt>
 *
 * <dd>在Executor关闭时，方法{@link #execute（Runnable）}中提交的新任务将被<em>拒绝</ em>
 * ，并且当Executor对最大线程和工作队列使用有限边界时容量，并且已经饱和。
 * 在任何一种情况下，{@code execute}方法都会调用其{@link RejectedExecutionHandler}的
 * {@link RejectedExecutionHandler＃rejectedExecution（Runnable，ThreadPoolExecutor）}方法。
 * 提供了四种预定义的处理程序策
 *
 * <ol>
 *
 * <li>在默认的{@link ThreadPoolExecutor.AbortPolicy}中，
 * 处理程序在拒绝时抛出运行时{@link RejectedExecutionException}。 </ LI>
 *
 * <li>在{@link ThreadPoolExecutor.CallerRunsPolicy}中，
 * 调用{@code execute}本身的线程运行该任务。这提供了一种简单的反馈控制机制，可以降低新任务的提交速度。 </ LI>
 *
 * <li>在{@link ThreadPoolExecutor.DiscardPolicy}中，简单地删除了无法执行的任务。 </ LI>
 *
 * <li>在{@link ThreadPoolExecutor.DiscardOldestPolicy}中，如果执行程序未关闭，
 * 则会删除工作队列头部的任务，然后重试执行（可能会再次失败，导致重复执行）。 </ LI>
 *
 * </ol>
 * <p>
 * 可以定义和使用其他类型的{@link RejectedExecutionHandler}类。
 * 这样做需要一些小心，特别是当策略设计为仅在特定容量或排队策略下工作时。 </ DD>
 *
 * <dt>钩子方法</dt>
 *
 * <dd>此类提供在执行每个任务之前和之后调用的{@code protected}可覆盖
 * {@link #beforeExecute（Thread，Runnable）}和{@link #afterExecute（Runnable，Throwable）}方法。
 * 这些可以用来操纵执行环境;例如，
 * 重新初始化ThreadLocals，收集统计信息或添加日志条目。
 * 此外，可以重写方法{@link #terminated}以执行Executor完全终止后需要执行的任何特殊处理。
 *
 * <p>如果钩子或回调方法抛出异常，内部工作线程可能会失败并突然终止。</ dd>
 *
 * <dt>队列维护</dt>
 *
 * <dd>方法{@link #getqueue（）}允许访问工作队列以进行监视和调试。强烈建议不要将此方法用于任何其他目的。
 * 当大量排队的任务被取消时，两个提供的方法{@link #remove（runnable）}和{@link #purge}可用于协助存储回收。</ dd>
 *
 * <dt>定稿</dt>
 *
 * <dd>程序<em> AND </ em>中不再引用的池没有剩余的线程将自动{@code shutdown}。
 * 如果您希望确保即使用户忘记调用{@link #shutdown}也会回收未引用的池，那么您必须通过设置适当的保持活动时间，
 * 使用零核心线程的下限来安排未使用的线程最终死亡和/或设置{@link #allowCoreThreadTimeOut（boolean）}。 </ DD>
 *
 * </dl>
 *
 * <p><b>Extension example</b>. 这个类的大多数扩展
 * 覆盖一个或多个受保护的钩子方法。例如，
 * 这是一个添加简单暂停/恢复功能的子类：
 *
 * <pre> {@code
 * class PausableThreadPoolExecutor extends ThreadPoolExecutor {
 *   private boolean isPaused;
 *   private ReentrantLock pauseLock = new ReentrantLock();
 *   private Condition unpaused = pauseLock.newCondition();
 *
 *   public PausableThreadPoolExecutor(...) { super(...); }
 *
 *   protected void beforeExecute(Thread t, Runnable r) {
 *     super.beforeExecute(t, r);
 *     pauseLock.lock();
 *     try {
 *       while (isPaused) unpaused.await();
 *     } catch (InterruptedException ie) {
 *       t.interrupt();
 *     } finally {
 *       pauseLock.unlock();
 *     }
 *   }
 *
 *   public void pause() {
 *     pauseLock.lock();
 *     try {
 *       isPaused = true;
 *     } finally {
 *       pauseLock.unlock();
 *     }
 *   }
 *
 *   public void resume() {
 *     pauseLock.lock();
 *     try {
 *       isPaused = false;
 *       unpaused.signalAll();
 *     } finally {
 *       pauseLock.unlock();
 *     }
 *   }
 * }}</pre>
 *
 * @author Doug Lea
 * public class ThreadPoolExecutor extends AbstractExecutorService {}
 * <p>
 * <p>
 * <p>
 * <p>
 * 使用给定的初始值创建一个新的{@code ThreadPoolExecutor}
 * 参数。
 * @param corePoolSize 除非设置了{@code allowCoreThreadTimeOut}，否则即使它们处于空闲状态，也要保留在池中的线​​程数
 * @param maximumPoolSize 池中允许的最大线程数
 * @param keepAliveTime 当线程数大于核心时，这是多余空闲线程在终止之前等待新任务的最长时间。
 * @param unit {@code keepAliveTime}参数的时间单位
 * @param workQueue 在执行任务之前用于保存任务的队列。此队列仅包含{@code execute}方法提交的{@code Runnable}任务。
 * @param threadFactory 在执行程序创建新线程时使用的工厂
 * @param handler 执行被阻止时使用的处理程序，因为已达到线程边界和队列容量
 * @throws IllegalArgumentException if one of the following holds:<br>
 * {@code corePoolSize < 0}<br>
 * {@code keepAliveTime < 0}<br>
 * {@code maximumPoolSize <= 0}<br>
 * {@code maximumPoolSize < corePoolSize}
 * @throws NullPointerException if {@code workQueue}
 * or {@code threadFactory} or {@code handler} is null
 *
 * <pre>
 *   public ThreadPoolExecutor(int corePoolSize,
 *                         int maximumPoolSize,
 *                         long keepAliveTime,
 *                         TimeUnit unit,
 *                         BlockingQueue<Runnable> workQueue,
 *                         ThreadFactory threadFactory,
 *                         RejectedExecutionHandler handler) {
 *   if (corePoolSize < 0 ||
 *           maximumPoolSize <= 0 ||
 *           maximumPoolSize < corePoolSize ||
 *           keepAliveTime < 0)
 *       throw new IllegalArgumentException();
 *   if (workQueue == null || threadFactory == null || handler == null)
 *       throw new NullPointerException();
 *   this.acc = System.getSecurityManager() == null ?
 *           null :
 *           AccessController.getContext();
 *   this.corePoolSize = corePoolSize;
 *   this.maximumPoolSize = maximumPoolSize;
 *   this.workQueue = workQueue;
 *   this.keepAliveTime = unit.toNanos(keepAliveTime);
 *   this.threadFactory = threadFactory;
 *   this.handler = handler;
 * }
 * </pre>
 * @author Doug Lea
 * @param <E> the type of elements held in this collection
 * <p>
 * public interface BlockingQueue<E> extends Queue<E> {
 * @since 1.5
 * <p>
 * <p>
 * <p>
 * <p>
 * 一个{@link java.util.Queue}，它还支持在检索元素时等待队列变为非空的操作，
 * 并在存储元素时等待队列中的空间可用。
 * <p> {@code BlockingQueue}方法有四种形式，有不同的处理操作的方式，
 * 不能立即满足，但可能在将来的某个时候得到满足：
 * 一个抛出异常，
 * 第二个返回一个特殊值（或者{@code null}或{@code false}，取决于操作）
 * ，第三个会无限期地阻塞当前线程，直到操作成功，
 * 并且第四个块在放弃之前仅限于给定的最大时间限制。
 * 这些方法总结在下表中：
 * <p>
 * <table BORDER CELLPADDING=3 CELLSPACING=1>
 * <caption>Summary of BlockingQueue methods</caption>
 * <tr>
 * <td></td>
 * <td ALIGN=CENTER><em>Throws exception</em></td>
 * <td ALIGN=CENTER><em>Special value</em></td>
 * <td ALIGN=CENTER><em>Blocks</em></td>
 * <td ALIGN=CENTER><em>Times out</em></td>
 * </tr>
 * <tr>
 * <td><b>Insert</b></td>
 * <td>{@link #add add(e)}</td>
 * <td>{@link #offer offer(e)}</td>
 * <td>{@link #put put(e)}</td>
 * <td>{@link #offer(Object, long, TimeUnit) offer(e, time, unit)}</td>
 * </tr>
 * <tr>
 * <td><b>Remove</b></td>
 * <td>{@link #remove remove()}</td>
 * <td>{@link #poll poll()}</td>
 * <td>{@link #take take()}</td>
 * <td>{@link #poll(long, TimeUnit) poll(time, unit)}</td>
 * </tr>
 * <tr>
 * <td><b>Examine</b></td>
 * <td>{@link #element element()}</td>
 * <td>{@link #peek peek()}</td>
 * <td><em>not applicable</em></td>
 * <td><em>not applicable</em></td>
 * </tr>
 * </table>
 *
 * <p> {@code BlockingQueue}不接受{@code null}元素。尝试{@code add}，
 * {@code put}或{@code offer} {@code null}时，实现抛出{@code NullPointerException}。
 * {@code null}用作标记值，表示{@code poll}操作失败。
 * <p> {@code BlockingQueue}可能是容量限制的。
 * 在任何给定时间它可能具有{@code remainingCapacity}，超过该值就不会有{@code put}而不会阻塞。
 * 没有任何内在容量限制的{@code BlockingQueue}始终报告{@code Integer.MAX_VALUE}的剩余容量。
 * <p> {@code BlockingQueue}实现主要用于生产者 - 消费者队列，
 * 但另外支持{@link java.util.Collection}接口。
 * 因此，例如，可以使用{@code remove（x）}从队列中删除任意元素。
 * 然而，这些操作通常<em>不</ em>非常有效地执行，并且仅用于偶尔使用，
 * 例如当排队的消息被取消时。 <p> {@code BlockingQueue}实现是线程安全的。
 * 所有排队方法都使用内部锁或其他形式的并发控制以原子方式实现其效果。
 * 但是，<em>批量</em>收集操作{@code addAll}，{@code containsAll}，
 * {@code retainAll}和{@code removeAll} <em>不</ em>必须以原子方式执行
 * ，除非指定否则在实施中。因此，例如，{@code addAll（c）}在仅添加{@code c}中的一些元素后失败（抛出异常）是可能的。
 * <p> {@code BlockingQueue} <em>不</ em>本质上支持任何类型的“关闭”或“关闭”操作以指示不再添加任何项目。
 * 这些功能的需求和使用倾向于依赖于实现。例如，一种常见的策略是生产者插入特殊的<em>流结束</ em>或<em>毒药</ em>对象，
 * 这些对象在消费者采用时会相应地进行解释。
 *
 * <p>
 * Usage example, based on a typical producer-consumer scenario.
 * Note that a {@code BlockingQueue} can safely be used with multiple
 * producers and multiple consumers.
 * <pre> {@code
 * class Producer implements Runnable {
 *   private final BlockingQueue queue;
 *   Producer(BlockingQueue q) { queue = q; }
 *   public void run() {
 *     try {
 *       while (true) { queue.put(produce()); }
 *     } catch (InterruptedException ex) { ... handle ...}
 *   }
 *   Object produce() { ... }
 * }
 *
 * class Consumer implements Runnable {
 *   private final BlockingQueue queue;
 *   Consumer(BlockingQueue q) { queue = q; }
 *   public void run() {
 *     try {
 *       while (true) { consume(queue.take()); }
 *     } catch (InterruptedException ex) { ... handle ...}
 *   }
 *   void consume(Object x) { ... }
 * }
 *
 * class Setup {
 *   void main() {
 *     BlockingQueue q = new SomeQueueImplementation();
 *     Producer p = new Producer(q);
 *     Consumer c1 = new Consumer(q);
 *     Consumer c2 = new Consumer(q);
 *     new Thread(p).start();
 *     new Thread(c1).start();
 *     new Thread(c2).start();
 *   }
 * }}</pre>
 *
 * <p>Memory consistency effects: As with other concurrent
 * collections, actions in a thread prior to placing an object into a
 * {@code BlockingQueue}
 * <a href="package-summary.html#MemoryVisibility"><i>happen-before</i></a>
 * actions subsequent to the access or removal of that element from
 * the {@code BlockingQueue} in another thread.
 *
 * <p>This interface is a member of the
 * <a href="{@docRoot}/../technotes/guides/collections/index.html">
 * Java Collections Framework</a>.
 * @since 1.5
 */
