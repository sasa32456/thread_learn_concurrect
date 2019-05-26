package com.n33.jcu.utils.block.semaphore;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * connection pool
 * 1.得不到连接，1000MS ,抛出异常
 * 2.blocking
 * 3.discard
 * 4.Get then throw exception
 * 5.get -> register the callback, -> call you.
 * <p>
 * <p>
 * <p>
 * 一次拿多个,释放多个，下个可以用
 * 反之释放少，一个会一直等待
 * 相对，释放多会出现最后许可大于初始的情况？？？
 *
 * @author N33
 * @date 2019/5/25
 */
public class SemaphoreExample2 {

    public static void main(String[] args) throws InterruptedException {
        final Semaphore semaphore = new Semaphore(1);

        for (int i = 0; i < 2; i++) {

            new Thread(() -> {
                System.out.println(Thread.currentThread().getName() + " in");
                try {
                    semaphore.acquire();
                    System.out.println(Thread.currentThread().getName() + " Get the semaphore");
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    semaphore.release();
                }

                System.out.println(Thread.currentThread().getName() + " out");
            }).start();

        }
        while (true) {


            System.out.println("AP->当前可用的许可证->" + semaphore.availablePermits());
            System.out.println("AP->当前可用的许可证->" + semaphore.getQueueLength());
            System.out.println("==============================");

            TimeUnit.SECONDS.sleep(1);
        }


    }
}
/**
 * 返回等待获取的线程数的估计值。
 * 该值仅是估计值，因为线程数可能
 * 此方法遍历内部数据时动态更改
 * 结构。该方法设计用于监测
 * 系统状态，不用于同步控制。
 *
 * @return the estimated number of threads waiting for this lock
 * public final int getQueueLength() {
 * return sync.getQueueLength();
 * }
 * 返回此信号量中可用的当前许可数。
 *
 * <p>This method is typically used for debugging and testing purposes.
 * @return the number of permits available in this semaphore
 * public int availablePermits() {
 * return sync.getPermits();
 * }
 */
