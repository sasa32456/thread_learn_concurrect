package com.n33.jcu.utils.block.semaphore;

import java.util.Collection;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class SemaphoreExample4 {

    public static void main(String[] args) throws InterruptedException {

        final MySemaphore semaphore = new MySemaphore(5);

        final Thread t1 = new Thread(() -> {
            try {
                semaphore.drainPermits();
                System.out.println(semaphore.availablePermits());
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                semaphore.release(5);
            }
            System.out.println("T1 finished");
        });
        t1.start();

        TimeUnit.MICROSECONDS.sleep(50);


        final Thread t2 = new Thread(() -> {
            try {
                //拿不到直接返回,无打断
                //final boolean success = semaphore.tryAcquire();
                //拿不到直接返回,有打断
                final boolean success = semaphore.tryAcquire(1,TimeUnit.SECONDS);
                System.out.println(success ? "Successful" : "Failure");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                semaphore.release();
            }
            System.out.println("T2 finished");
        });
        t2.start();


        TimeUnit.SECONDS.sleep(1);

        System.out.println(semaphore.hasQueuedThreads());

        final Collection<Thread> waitingThreads = semaphore.getWaitingThreads();
        for (Thread t : waitingThreads) {
            System.out.println(t);
        }
    }

    static class MySemaphore extends Semaphore {
        public MySemaphore(int permits) {
            super(permits);
        }

        public MySemaphore(int permits, boolean fair) {
            super(permits, fair);
        }


        public Collection<Thread> getWaitingThreads() {
            return super.getQueuedThreads();
        }


    }

}
/**
 * 获取并返回所有可立即获得的许可。
 *
 * @return the number of permits acquired
 * public int drainPermits() {
 * return sync.drainPermits();
 * }
 * 查询是否有任何线程正在等待获取。注意
 * 因为取消可能随时发生，{@code true} return不保证任何其他线程永远不会
 * 获得。此方法主要用于
 * 监控系统状态。
 * @return {@code true} if there may be other threads waiting to
 * acquire the lock
 * <p>
 * public final boolean hasQueuedThreads() {
 * return sync.hasQueuedThreads();
 * }
 * 返回包含可能正在等待获取的线程的集合。
 * 因为实际的线程集可能会动态变化
 * 构建此结果，返回的集合只是最好的努力
 * 估计。返回集合的元素没有特别之处
 * 订单。该方法旨在便于施工
 * 提供更广泛监控设施的子类。
 * @return the collection of threads
 * protected Collection<Thread> getQueuedThreads() {
 * return sync.getQueuedThreads();
 * }
 * 如果此信号量的公平性设置为true，则返回{@code true}。
 * @return {@code true}如果此信号量的公平性设置为真
 * public boolean isFair() {
 * return sync instanceof FairSync;
 * }



 * 从这个信号量获取许可证，只有在该信号量可用时才获得许可证
 *调用时间。
 *
 * <p>获得许可证（如果有）并立即返回，
 *值为{@code true}，
 *减少一个可用许可证的数量。
 *
 * <p>如果没有许可证，则此方法将返回
 *立即使用值{@code false}。
 *
 * <p>即使已将此信号量设置为使用a
 *公平订购政策，致电{@code tryAcquire（）} <em>将</ em>
 *无论是否可用，立即获得许可
 *其他线程目前正在等待。
 *这个“闯入”行为在某些方面可能有用
 *情况，即使它打破了公平。如果你想兑现
 *公平性设置，然后使用
 * {@link #tryAcquire（long，TimeUnit）tryAcquire（0，TimeUnit.SECONDS）}
 *几乎相当（它也检测中断）。
 *
 * @return {@code true} if a permit was acquired and {@code false}
 *         otherwise
 *
 * public boolean tryAcquire() {
 *     return sync.nonfairTryAcquireShared(1) >= 0;
 * }
 */
