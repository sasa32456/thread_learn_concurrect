package com.n33.jcu.utils.locks.reentrantlock;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 公平锁
 *
 * @author N33
 * @date 2019/5/26
 */
public class ReentrantLockExample {

    private static final ReentrantLock lock = new ReentrantLock();

    public static void main(String[] args) throws InterruptedException {

//        IntStream.range(0, 2).forEach(i -> new Thread(() -> {
//           //needLock();
//            needLockBySync();
//        }).start());
//__________________________________________________________________________________
//        final Thread thread1 = new Thread(() -> testUnInterruptibly());
//        thread1.start();
//
//        TimeUnit.SECONDS.sleep(1);
//
//        final Thread thread2 = new Thread(() -> testUnInterruptibly());
//        thread2.start();
//
//        TimeUnit.SECONDS.sleep(1);
//        thread2.interrupt();
//        System.out.println("=================");


//        --------------------------------------------------
//        final Thread thread1 = new Thread(() -> testTryLock());
//        thread1.start();
//
//        TimeUnit.SECONDS.sleep(1);
//
//        final Thread thread2 = new Thread(() -> testTryLock());
//        thread2.start();



        final Thread thread1 = new Thread(() -> testUnInterruptibly());
        thread1.start();

        TimeUnit.SECONDS.sleep(1);

        final Thread thread2 = new Thread(() -> testUnInterruptibly());
        thread2.start();

        TimeUnit.SECONDS.sleep(1);

        //有几个等待
        Optional.of(lock.getQueueLength()).ifPresent(System.out::println);
        //是否有等待
        Optional.of(lock.hasQueuedThreads()).ifPresent(System.out::println);

        //所选线程是否等待
        Optional.of(lock.hasQueuedThread(thread1)).ifPresent(System.out::println);
        Optional.of(lock.hasQueuedThread(thread2)).ifPresent(System.out::println);

        //是否被其他线程lock
        Optional.of(lock.isLocked()).ifPresent(System.out::println);




    }


    public static void testTryLock() {

        if (lock.tryLock()) {
            try {
                Optional.of("The Thread-" + Thread.currentThread().getName() + " get lock and will do working...").ifPresent(System.out::println);
                while (true) {

                }
            } finally {
                lock.unlock();
            }
        } else {
            Optional.of("The Thread-" + Thread.currentThread().getName() + " not get lock ...").ifPresent(System.out::println);
        }
    }


    public static void testUnInterruptibly() {
        try {
            //不可被打断
            //lock.lock();
            //可以被打断
            lock.lockInterruptibly();
            //拿到锁的次数
            Optional.of( Thread.currentThread().getName()+":"+lock.getHoldCount()).ifPresent(System.out::println);
            Optional.of("The Thread-" + Thread.currentThread().getName() + " get lock and will do working...").ifPresent(System.out::println);
            while (true) {

            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public static void needLock() {
        try {
            lock.lock();
            Optional.of("The Thread-" + Thread.currentThread().getName() + " get lock and will do working...").ifPresent(System.out::println);
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }


    /**
     * 等价但性能不一样
     */
    public static void needLockBySync() {
        synchronized (ReentrantLockExample.class) {
            try {
                Optional.of("The Thread-" + Thread.currentThread().getName() + " get lock and will do working...").ifPresent(System.out::println);
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


}
/**
 * 可重入互斥{@link Lock}具有与使用{@code synchronized}方法和语句访问的隐式监视器锁相同的基本行为和语义，但具有扩展功能。
 * <p>线程上次成功锁定时，{@code ReentrantLock} <em>拥有</ em>，但尚未解锁。
 * 当锁不是由另一个线程拥有时，调用{@code lock}的线程将返回，成功获取锁。
 * 如果当前线程已拥有锁，则该方法将立即返回。
 * 可以使用{@link #isHeldByCurrentThread}和{@link #getHoldCount}方法检查。
 * <p>此类的构造函数接受可选的<em> fairness </ em>参数。
 * 当设置{@code true}时，在争用下，锁有利于授予对等待时间最长的线程的访问权限。
 * 否则，此锁定不保证任何特定的访问顺序。
 * 使用由许多线程访问的公平锁的程序可以显示比使用默认设置的程序更低的总吞吐量（即，更慢;通常慢得多），
 * 但是获得锁的时间变化较小并且保证缺乏饥饿。
 * 但请注意，锁的公平性并不能保证线程调度的公平性。
 * 因此，使用公平锁定的许多线程中的一个可以连续多次获得它，而其他活动线程没有进展并且当前没有保持锁定。
 * 另请注意，不定时的{@link #tryLock（）}方法不符合公平性设置。
 * 如果锁即使可用，即使其他线程正在等待，它也会成功。
 *
 * <p>建议练习<em>始终</ em>立即跟随{@code lock}调用{@code try}块，最常见的是在前/后构造中，例如：
 *
 * <pre> {@code
 * class X {
 *   private final ReentrantLock lock = new ReentrantLock();
 *   // ...
 *
 *   public void m() {
 *     lock.lock();  // block until condition holds
 *     try {
 *       // ... method body
 *     } finally {
 *       lock.unlock()
 *     }
 *   }
 * }}</pre>
 *
 * <p>除了实现{@link Lock}接口之外，该类还定义了许多{@code public}和{@code protected}方法来检查锁的状态。其中一些方法仅适用于仪器和监测。
 *
 * <p>此类的序列化与内置锁的行为方式相同：反序列化锁处于解锁状态，无论序列化时的状态如何。
 *
 * <p>此锁通过同一个线程最多支持2147483647个递归锁。尝试超过此限制会导致锁定方法引发{@link Error}。
 *
 * @author Doug Lea
 * public class ReentrantLock implements Lock, java.io.Serializable {
 * @since 1.5
 *
 *
 * 查询当前线程对此锁定的保持数。
 * <p>线程对每个与解锁操作不匹配的锁定操作都有一个锁定。
 *
 * <p>保持计数信息通常仅用于测试和调试目的。例如，如果不应该使用已经保存的锁输入某段代码，那么我们可以声明这一事实：
 *
 *  <pre> {@code
 * class X {
 *   ReentrantLock lock = new ReentrantLock();
 *   // ...
 *   public void m() {
 *     assert lock.getHoldCount() == 0;
 *     lock.lock();
 *     try {
 *       // ... method body
 *     } finally {
 *       lock.unlock();
 *     }
 *   }
 * }}</pre>
 *
 * @return 当前线程锁定此锁定的次数，如果当前线程未保持此锁定，则为零
 *         public int getHoldCount() {
 *     return sync.getHoldCount();
 * }
 */
