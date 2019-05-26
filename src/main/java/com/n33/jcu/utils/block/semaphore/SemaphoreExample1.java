package com.n33.jcu.utils.block.semaphore;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * 设置访问许可
 * 数量，等，访问不了的放入队列
 * 当锁等
 *
 * @author N33
 * @date 2019/5/24
 */
public class SemaphoreExample1 {
    public static void main(String[] args) {
        final SemaphoreLock lock = new SemaphoreLock();


        IntStream.range(0, 2).forEach(value -> new Thread(() -> {
            try {
                System.out.println(Thread.currentThread().getName() + " is Running");
                lock.lock();
                System.out.println(Thread.currentThread().getName() + " get the #SemaphoreLock");
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
            System.out.println(Thread.currentThread().getName() + " released the #SemaphoreLock");
        }).start());


    }

    static class SemaphoreLock {
        private final Semaphore semaphore = new Semaphore(1);

        public void lock() throws InterruptedException {
            //申请许可
            semaphore.acquire();
        }

        public void unlock() {
            semaphore.release();
        }
    }

    /**
     * synchronized只能一个
     */
    private synchronized static void m() {
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
/**
 * 计数信号量。从概念上讲，信号量保持一组许可。
 * 如果需要，每个{@link #acquire}都会阻止，直到有许可证可用，然后接受它。
 * 每个{@link #release}都会添加一个许可证，可能会释放阻塞收单机构。
 * 但是，没有使用实际的许可对象;
 * {@code Semaphore}只保留可用数量的计数并相应地采取行动。
 *
 * <p>信号量通常用于限制线程数，而不是访问某些（物理或逻辑）资源。
 * 例如，这是一个使用信号量来控制对项池的访问的类：
 * <pre> {@code
 * class Pool {
 *   private static final int MAX_AVAILABLE = 100;
 *   private final Semaphore available = new Semaphore(MAX_AVAILABLE, true);
 *
 *   public Object getItem() throws InterruptedException {
 *     available.acquire();
 *     return getNextAvailableItem();
 *   }
 *
 *   public void putItem(Object x) {
 *     if (markAsUnused(x))
 *       available.release();
 *   }
 *
 *   // Not a particularly efficient data structure; just for demo
 *
 *   protected Object[] items = ... whatever kinds of items being managed
 *   protected boolean[] used = new boolean[MAX_AVAILABLE];
 *
 *   protected synchronized Object getNextAvailableItem() {
 *     for (int i = 0; i < MAX_AVAILABLE; ++i) {
 *       if (!used[i]) {
 *          used[i] = true;
 *          return items[i];
 *       }
 *     }
 *     return null; // not reached
 *   }
 *
 *   protected synchronized boolean markAsUnused(Object item) {
 *     for (int i = 0; i < MAX_AVAILABLE; ++i) {
 *       if (item == items[i]) {
 *          if (used[i]) {
 *            used[i] = false;
 *            return true;
 *          } else
 *            return false;
 *       }
 *     }
 *     return false;
 *   }
 * }}</pre>
 *
 * <p>在获取项目之前，每个线程必须从信号量获取许可证，以保证项目可供使用。
 * 当线程完成项目后，它将返回到池中，并且许可证将返回到信号量，允许另一个线程获取该项目。
 * 请注意，调用{@link #acquire}时不会保持同步锁定，因为这会阻止项目返回池中。
 * 信号量封装了限制访问池所需的同步，与维护池本身一致性所需的任何同步分开。
 *
 * <p>信号量初始化为1，并且使用的信号量最多只有一个许可证可用作互斥锁。
 * 这通常称为<em>二进制信号量</ em>，因为它只有两种状态：
 * 一种是可用的，或者是零可用的。
 * 当以这种方式使用时，二进制信号量具有属性（与许多{@link java.util.concurrent.locks.Lock}实现不同），
 * 即“锁定”信号。可以由所有者以外的线程释放（因为信号量没有所有权的概念）。
 * 这在某些特定的上下文中很有用，例如死锁恢复。
 *
 * <p>此类的构造函数可选择接受<em> fairness </ em>参数。
 * * 设置为false时，此类不保证线程获取许可的顺序。
 * * 特别是，允许​​<em> barging </ em>，也就是说，调用{@link #acquire}的线程可以在等待的线程之前分配许可
 * * - 逻辑上新线程将自己放在头部等待线程的队列。当公平性设置为true时，
 * * 信号量保证选择调用任何{@link #acquire（）acquire}方法的线程，
 * * 以按照处理这些方法的调用的顺序获得许可（先进先出） ; FIFO）。
 * * 请注意，FIFO排序必然适用于这些方法中的特定内部执行点。
 * * 因此，一个线程可以在另一个线程之前调用{@code acquire}，但是在另一个线程之后到达排序点，
 * * 并且类似地从该方法返回时。另请注意，不定时的{@link #tryAcquire（）tryAcquire}方法不遵守公平性设置，但会采用任何可用的许可。
 * *
 * * <p>通常，用于控制资源访问的信号量应初始化为公平，以确保没有线程缺乏访问资源。
 * * 当将信号量用于其他类型的同步控制时，非公平排序的吞吐量优势通常超过公平性考虑。
 * *
 * * <p>该类还为{@link #acquire（int）acquire}和{@link #release（int）release}提供了一次多个许可的便捷方法。
 * * 当使用这些方法而没有公平地设定时，要注意无限期推迟的风险增加。
 * *
 * * <p>内存一致性效果：在调用“发布”方法之前线程中的操作，例如{@code release（）}
 * * <a href="package-summary.html#MemoryVisibility"><i>发生之前</i></a>
 * * 在另一个线程中成功的“获取”方法（例如{@code acquire（）}之后的操作。
 * *
 *
 * @author Doug Lea
 * public class Semaphore implements java.io.Serializable {
 * @since 1.5
 */

