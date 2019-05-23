package com.n33.jcu.utils.cyclicbarrier;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;

/**
 *
 * 拦住，等都到了再执行
 * 区别是没有计数
 *
 * @author N33
 * @date 2019/5/23
 */
public class CyclicBarrierExample1 {


    public static void main(String[] args) throws BrokenBarrierException, InterruptedException {

        //final CyclicBarrier cyclicBarrier = new CyclicBarrier(2);

        //回调
        final CyclicBarrier cyclicBarrier = new CyclicBarrier(2, () -> System.out.println("all of finished."));

        new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(5);
                System.out.println("T1: finished");
                cyclicBarrier.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }

        }).start();

        new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
                System.out.println("T2: finished");
                cyclicBarrier.await();
                System.out.println("T2: The other thread finished too.");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }

        }).start();


        while (true) {
            System.out.println(cyclicBarrier.getNumberWaiting());
            System.out.println(cyclicBarrier.getParties());
            System.out.println(cyclicBarrier.isBroken());
            TimeUnit.MILLISECONDS.sleep(100);
        }

    }

}
/**
 * 允许一组线程等待的同步辅助
 *彼此达成共同的障碍点。 CyclicBarriers是
 *在涉及固定大小的线程方的程序中很有用
 *必须偶尔等待对方。屏障被称为
 * <em>循环</ em>因为它可以在等待的线程之后重新使用
 *被释放。
 *
 * <p> {@code CyclicBarrier}支持可选的{@link Runnable}命令
 *在每个障碍点运行一次，在聚会中的最后一个帖子之后
 *到达，但在任何线程发布之前。
 *此<em>屏障操作</ em>非常有用
 *用于在任何一方继续之前更新共享状态。
 *
 * <p> <b>样本用法：</ b>以下是在a中使用屏障的示例
 *并行分解设计：
 *
 *  <pre> {@code
 * class Solver {
 *   final int N;
 *   final float[][] data;
 *   final CyclicBarrier barrier;
 *
 *   class Worker implements Runnable {
 *     int myRow;
 *     Worker(int row) { myRow = row; }
 *     public void run() {
 *       while (!done()) {
 *         processRow(myRow);
 *
 *         try {
 *           barrier.await();
 *         } catch (InterruptedException ex) {
 *           return;
 *         } catch (BrokenBarrierException ex) {
 *           return;
 *         }
 *       }
 *     }
 *   }
 *
 *   public Solver(float[][] matrix) {
 *     data = matrix;
 *     N = matrix.length;
 *     Runnable barrierAction =
 *       new Runnable() { public void run() { mergeRows(...); }};
 *     barrier = new CyclicBarrier(N, barrierAction);
 *
 *     List<Thread> threads = new ArrayList<Thread>(N);
 *     for (int i = 0; i < N; i++) {
 *       Thread thread = new Thread(new Worker(i));
 *       threads.add(thread);
 *       thread.start();
 *     }
 *
 *     // wait until done
 *     for (Thread thread : threads)
 *       thread.join();
 *   }
 * }}</pre>
 *
 * 在这里，每个工作线程处理一行矩阵然后等待
 *屏障直到所有行都被处理完毕。处理完所有行时
 *执行提供的{@link Runnable}屏障操作并合并
 *行。如果合并
 *确定已找到解决方案，然后{@code done（）}将返回
 * {@code true}，每个工作人员都将终止。
 *
 * <p>如果屏障行动不依赖于当事人被停职的时候
 *它被执行，然后该方中的任何线程都可以执行该操作
 *发布时的行动。为了方便这一点，每次调用
 * {@link #await}返回该线程在屏障处的到达索引。
 *然后，您可以选择执行屏障操作的线程
 *示例：
 *  <pre> {@code
 * if (barrier.await() == 0) {
 *   // log the completion of this iteration
 * }}</pre>
 *
 *<p> {@code CyclicBarrier}使用全有或全无破坏模型
 *失败同步尝试：如果线程离开障碍
 *由于中断，故障或超时而过早地指向所有
 *在该障碍点等待的其他线程也将离开
 *异常通过{@link BrokenBarrierException}（或
 * {@link InterruptedException}如果他们也被打断了
 * 同一时间）。
 *
 * <p>内存一致性效果：调用前线程中的操作
 * {@code await（）}
 * <a href="package-summary.html#MemoryVisibility"> <i>发生之前</ i> </a>
 *作为障碍行动一部分的行动，反过来
 * <i>在成功返回之后的</ i>行动之前发生
 *在其他线程中对应{@code await（）}。
 *
 * @since 1.5
 * @see CountDownLatch
 *
 * @author Doug Lea
 * public class CyclicBarrier {
 */
