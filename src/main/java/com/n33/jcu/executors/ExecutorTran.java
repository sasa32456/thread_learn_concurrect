package com.n33.jcu.executors;

public class ExecutorTran {
}
/**
 * 执行提交的{@link Runnable}任务的对象。
 * 此接口提供了一种将任务提交与每个任务的运行机制分离的方法，包括线程使用，调度等的详细信息。
 * 通常使用{@code Executor}而不是显式创建线程。
 * 例如，不是为一组任务调用{@code new Thread（new（RunnableTask（）））。start（）}，而是使用：
 *
 * <pre>
 * Executor executor = <em>anExecutor</em>;
 * executor.execute(new RunnableTask1());
 * executor.execute(new RunnableTask2());
 * ...
 * </pre>
 *
*但是，{@code Executor}界面并不严格
 *要求执行是异步的。在最简单的情况下，一个
 * executor可以立即在调用者中运行提交的任务
 *线程：
 *
 *  <pre> {@code
 * class DirectExecutor implements Executor {
 *   public void execute(Runnable r) {
 *     r.run();
 *   }
 * }}</pre>
 *
 * More typically, tasks are executed in some thread other
 * than the caller's thread.  The executor below spawns a new thread
 * for each task.
 *
 *  <pre> {@code
 * class ThreadPerTaskExecutor implements Executor {
 *   public void execute(Runnable r) {
 *     new Thread(r).start();
 *   }
 * }}</pre>
 *
 * Many {@code Executor} implementations impose some sort of
 * limitation on how and when tasks are scheduled.  The executor below
 * serializes the submission of tasks to a second executor,
 * illustrating a composite executor.
 *
 *  <pre> {@code
 * class SerialExecutor implements Executor {
 *   final Queue<Runnable> tasks = new ArrayDeque<Runnable>();
 *   final Executor executor;
 *   Runnable active;
 *
 *   SerialExecutor(Executor executor) {
 *     this.executor = executor;
 *   }
 *
 *   public synchronized void execute(final Runnable r) {
 *     tasks.offer(new Runnable() {
 *       public void run() {
 *         try {
 *           r.run();
 *         } finally {
 *           scheduleNext();
 *         }
 *       }
 *     });
 *     if (active == null) {
 *       scheduleNext();
 *     }
 *   }
 *
 *   protected synchronized void scheduleNext() {
 *     if ((active = tasks.poll()) != null) {
 *       executor.execute(active);
 *     }
 *   }
 * }}</pre>
 *
 * 此包中提供的{@code Executor}实现实现了{@link ExecutorService}，这是一个更广泛的接口。
 * {@link ThreadPoolExecutor}类提供可扩展的线程池实现。
 * {@link Executors}类为这些Executor提供了方便的工厂方法。
 * <p>内存一致性效果：在将{@code Runnable}对象提交到{@code Executor}
 * <a href="package-summary.html#MemoryVisibility"> <i>之前发生之前，
 * 线程中的操作</ i> </a>它的执行开始，也许是在另一个线程中。
 *
 * @since 1.5
 * @author Doug Lea
 * public interface Executor {}
 */
