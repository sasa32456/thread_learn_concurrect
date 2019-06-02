package com.n33.jcu.executors.future.future;

import java.util.concurrent.*;

/**
 * future解决方法串行同时,回头还能获得结果
 *
 * @author N33
 * @date 2019/6/2
 */
public class FutureExample1 {

    public static void main(String[] args) throws ExecutionException, InterruptedException, TimeoutException {
//        testGet();

        testGetTimeOut();
    }

    private static void testGet() throws ExecutionException, InterruptedException {
        ThreadPoolExecutor executorService = (ThreadPoolExecutor) Executors.newCachedThreadPool();

        Future<Integer> future = executorService.submit(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 10;
        });

        //======================================
        System.out.println("=======i will be printed quickly.===========");
        //======================================

        Thread callerThread = Thread.currentThread();


        /**
         * Exception in thread "main" java.lang.InterruptedException
         * 打断的是main线程
         */
        new Thread(() -> {
            try {
                TimeUnit.MILLISECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            callerThread.interrupt();
        }).start();



        //blocked,被打断的是main
        final Integer result = future.get();

        System.out.println("================="+result+"==============");
    }


    /**
     * dist cp  5 hour
     * 超时后报错，但依旧接着执行
     *
     * yarn applicationJobId
     *
     * process
     *
     * kill -9 process
     * yarn 0kill applicationJobId
     *
     *
     *
     * @throws ExecutionException
     * @throws InterruptedException
     * @throws TimeoutException
     */
    private static void testGetTimeOut() throws ExecutionException, InterruptedException, TimeoutException {
        ThreadPoolExecutor executorService = (ThreadPoolExecutor) Executors.newCachedThreadPool();

        Future<Integer> future = executorService.submit(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
                System.out.println("++++++++++++++++++++");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return 10;
        });

        //被timeout后依旧会执行，但无返回
        final Integer result = future.get(1,TimeUnit.SECONDS);

        System.out.println("================="+result+"==============");
    }

}
/**
 * {@code Future}表示异步计算的结果。提供方法以检查计算是否完成，
 * 等待其完成，以及检索计算结果。只有在计算完成后才能使用方法{@code get}检索结果，
 * 必要时阻塞直到准备就绪。取消由{@code cancel}方法执行。
 * 提供了其他方法来确定任务是否正常完成或被取消。
 * 计算完成后，无法取消计算。如果您想使用{@code Future}以获取可取消性但不提供可用结果，
 * 您可以声明{@code Future <？>}形式的类型并返回{@code null}作为结果基本任务。
 *
 * <p>
 * <b>样本用法</ b>（请注意，以下课程均已完成。)
 * <pre> {@code
 * interface ArchiveSearcher { String search(String target); }
 * class App {
 *   ExecutorService executor = ...
 *   ArchiveSearcher searcher = ...
 *   void showSearch(final String target)
 *       throws InterruptedException {
 *     Future<String> future
 *       = executor.submit(new Callable<String>() {
 *         public String call() {
 *             return searcher.search(target);
 *         }});
 *     displayOtherThings(); // do other things while searching
 *     try {
 *       displayText(future.get()); // use future
 *     } catch (ExecutionException ex) { cleanup(); return; }
 *   }
 * }}</pre>
 *
 * {@link FutureTask}类是实现{@code Runnable}的{@code Future}的实现，因此可以由{@code Executor}执行。
 *例如，{@code submit}的上述结构可以替换为：
 *  <pre> {@code
 * FutureTask<String> future =
 *   new FutureTask<String>(new Callable<String>() {
 *     public String call() {
 *       return searcher.search(target);
 *   }});
 * executor.execute(future);}</pre>
 *
 * <p>内存一致性效果：异步计算采取的操作
 * 在另一个线程中的相应{@code Future.get（）}之后<a href="package-summary.html#MemoryVisibility"> <i>发生</ i> </a>之前的操作。
 *
 * @see FutureTask
 * @see Executor
 * @since 1.5
 * @author Doug Lea
 * @param <V> The result type returned by this Future's {@code get} method
 *
 * public interface Future<V> {}
 */
