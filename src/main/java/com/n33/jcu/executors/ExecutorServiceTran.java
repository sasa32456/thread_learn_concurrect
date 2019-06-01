package com.n33.jcu.executors;

public class ExecutorServiceTran {
}
/**
 * 一个{@link Executor}，提供管理终止的方法和可以生成{@link Future}以跟踪一个或多个异步任务进度的方法。
 *
 * <p>可以关闭{@code ExecutorService}，这将导致它拒绝新任务。
 * 提供了两种不同的方法来关闭{@code ExecutorService}。
 * {@link #shutdown}方法将允许先前提交的任务在终止之前执行，
 * 而{@link #shutdownNow}方法阻止等待任务启动并尝试停止当前正在执行的任务。
 * 终止时，执行程序没有正在执行的任务，没有等待执行的任务，也没有任何新任务可以提交。
 * 应关闭未使用的{@code ExecutorService}以允许回收其资源。
 *
 * <p>方法{@code submit}通过创建并返回可用于取消执行和/或等待完成的{@link Future}来扩展基本方法{@link Executor #cute（Runnable）}。
 * 方法{@code invokeAny}和{@code invokeAll}执行最常用的批量执行形式，执行一组任务，然后等待至少一个或全部完成。
 * （类{@link ExecutorCompletionService}可用于编写这些方法的自定义变体。）
 *
 * <p>{@link Executors}类为此包中提供的执行程序服务提供工厂方法。
 *
 * <h3>Usage Examples</h3>
 *
 * 下面是网络服务的草图，其中线程池中的线程为传入请求提供服务。
 * 它使用预配置的{@link Executors＃newFixedThreadPool}工厂方法：
 *
 *  <pre> {@code
 * class NetworkService implements Runnable {
 *   private final ServerSocket serverSocket;
 *   private final ExecutorService pool;
 *
 *   public NetworkService(int port, int poolSize)
 *       throws IOException {
 *     serverSocket = new ServerSocket(port);
 *     pool = Executors.newFixedThreadPool(poolSize);
 *   }
 *
 *   public void run() { // run the service
 *     try {
 *       for (;;) {
 *         pool.execute(new Handler(serverSocket.accept()));
 *       }
 *     } catch (IOException ex) {
 *       pool.shutdown();
 *     }
 *   }
 * }
 *
 * class Handler implements Runnable {
 *   private final Socket socket;
 *   Handler(Socket socket) { this.socket = socket; }
 *   public void run() {
 *     // read and service request on socket
 *   }
 * }}</pre>
 *
 * 以下方法分两个阶段关闭{@code ExecutorService}，首先调用{@code shutdown}拒绝传入的任务，
 * 然后在必要时调用{@code shutdownNow}以取消任何延迟的任务：
 *
 *  <pre> {@code
 * void shutdownAndAwaitTermination(ExecutorService pool) {
 *   pool.shutdown(); // Disable new tasks from being submitted
 *   try {
 *     // Wait a while for existing tasks to terminate
 *     if (!pool.awaitTermination(60, TimeUnit.SECONDS)) {
 *       pool.shutdownNow(); // Cancel currently executing tasks
 *       // Wait a while for tasks to respond to being cancelled
 *       if (!pool.awaitTermination(60, TimeUnit.SECONDS))
 *           System.err.println("Pool did not terminate");
 *     }
 *   } catch (InterruptedException ie) {
 *     // (Re-)Cancel if current thread also interrupted
 *     pool.shutdownNow();
 *     // Preserve interrupt status
 *     Thread.currentThread().interrupt();
 *   }
 * }}</pre>
 *
 * <p>内存一致性效果：在将{@code Runnable}或{@code Callable}任务提交到{@code ExecutorService}
 * <a href="package-summary.html#MemoryVisibility">之前，
 * 线程中的操作<i>发生 - 之前</ i> </a>该任务采取的任何行动，
 * 反过来<i>发生 - 在</ i>之前通过{@code Future.get（）}检索结果。
 *
 * @since 1.5
 * @author Doug Lea
 * public interface ExecutorService extends Executor {}
 */
