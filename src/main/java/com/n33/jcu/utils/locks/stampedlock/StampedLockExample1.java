package com.n33.jcu.utils.locks.stampedlock;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.StampedLock;
import java.util.stream.Collectors;

/**
 * ReentrantLock VS Synchronized
 * <p>灵活扩展性能
 * <p>
 * ReentrantReadWriteLock
 * 同时读从而性能提升
 * <p>
 * 100 threads
 * 99 threads need read lock
 * 1 threads need write lock
 * 写饥饿，大量读不管写，抢锁抢不到
 * <p>
 * StampedLock 读写锁
 * 判断是否读写，如果写，等，没写，读
 * 完美替换读写锁
 *
 * @author N33
 * @date 2019/5/26
 */
public class StampedLockExample1 {

    private final static StampedLock lock = new StampedLock();
    private final static List<Long> DATA = new ArrayList<>();

    public static void main(String[] args) {

        final ExecutorService executor = Executors.newFixedThreadPool(10);

        Runnable readTask = () -> {
            for (; ; ) {
                read();
            }
        };

        Runnable writeTask = () -> {
            for (; ; ) {
                write();
            }
        };

        executor.submit(readTask);
        executor.submit(readTask);
        executor.submit(readTask);
        executor.submit(readTask);
        executor.submit(readTask);
        executor.submit(readTask);
        executor.submit(readTask);
        executor.submit(readTask);
        executor.submit(readTask);
        /**
         * 有问题，一直写。。。奇葩
         */
        executor.submit(writeTask);

//
//        for (int i = 0; i < 9; i++) {
//            new Thread(readTask).start();
//        }
//        new Thread(writeTask).start();

        System.out.println("start");

    }

    private static void read() {
        long stamped = -1;

        try {
            //悲观,相对写
            stamped = lock.readLock();
            Optional.of(Thread.currentThread().getName()+ " read : " +DATA.stream().map(String::valueOf).collect(Collectors.joining("#", "R-", ""))).ifPresent(System.out::println);
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlockRead(stamped);
        }

    }

    private static void write() {
        long stamp = -1;
        try {
            //悲观
            stamp = lock.writeLock();
            DATA.add(System.currentTimeMillis());
            System.out.println(Thread.currentThread().getName()+" is writing...");
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlockWrite(stamp);
        }

    }

}
/**
 * 基于功能的锁，具有三种控制读/写访问的模式。
 * StampedLock的状态包括版本和模式。
 * 锁定获取方法返回一个表示和控制锁定状态访问的标记;
 * 这些方法的“try”版本可能会返回特殊值零以表示无法获取访问权限。
 * 锁定释放和转换方法需要标记作为参数，如果它们与锁定状态不匹配则会失败。
 * 这三种模式是：
 * <ul> <li> <b>写入</b>方法{@link #writeLock}可能会阻塞等待独占访问，
 * 返回可以在方法{@link #unlockWrite}中使用的戳记释放锁。
 * 还提供了{@code tryWriteLock}的不定时和定时版本。
 * 当锁保持在写模式时，不能获得读锁，并且所有乐观读验证都将失败。
 * </li> <li> <b>阅读。</b>
 * 方法{@link #readLock}可能会阻塞等待非独占访问，
 * 返回可在方法{@link #unlockRead}中使用的戳记以释放锁。
 * 还提供了{@code tryReadLock}的不定时和定时版本。
 * </ li> <li> <b>乐观阅读。
 * </ b>方法{@link #tryOptimisticRead}仅在锁定当前未处于写入模式时才返回非零标记。
 * 如果在获取给定标记后未在写入模式下获取锁，则方法{@link #validate}返回true。
 * 这种模式可以被认为是读锁的极弱版本，可以随时由作者打破。
 * 对短的只读代码段使用乐观模式通常可以减少争用并提高吞吐量。
 * 但是，它的使用本质上是脆弱的。
 * 乐观读取部分应该只读取字段并将它们保存在局部变量中，以便以后在验证后使用。
 * 在乐观模式下读取的字段可能非常不一致，
 * 因此仅当您熟悉数据表示以检查一致性和/或重复调用方法{@code validate（）}时才会使用。
 * 例如，在首先读取对象或数组引用，然后访问其字段，元素或方法之一时，通常需要这些步骤。
 * </ li> </ ul> <p>此类还支持有条件地提供跨三种模式的转换的方法。
 * 例如，方法{@link #tryConvertToWriteLock}尝试“升级”模式，
 * 如果（1）已经处于读取模式的写入模式（2）且没有其他读取器或（3）处于乐观模式，
 * 则返回有效写入标记并且锁可用。这些方法的形式旨在帮助减少在基于重试的设计中出现的一些代码膨胀。
 * <p> StampedLocks设计用作开发线程安全组件的内部实用程序。
 * 它们的使用依赖于对它们所保护的数据，对象和方法的内部属性的了解。
 * 它们不是可重入的，因此锁定的主体不应该调用可能尝试重新获取锁的其他未知方法（尽管您可以将戳记传递给可以使用或转换它的其他方法）。
 * 读锁定模式的使用依赖于相关的代码部分是无副作用的。
 * 未经验证的乐观读取部分无法调用未知容忍潜在不一致的方法。
 * 标记使用有限表示，并且不是加密安全的（即，可以猜测有效标记）。
 * 印花值可在连续操作一年后（不迟于一年）后回收。
 * 未经使用或验证超过此期限而持有的印章可能无法正确验证。
 * StampedLocks是可序列化的，但总是反序列化为初始解锁状态，因此它们对远程锁定没有用。
 * <p> StampedLock的调度政策并不总是偏好读者而不是作者，反之亦然。所有“尝试”方法都是尽力而为，并不一定符合任何调度或公平政策。
 * 从任何“try”方法获取或转换锁定的零返回不会携带有关锁定状态的任何信息;后续调用可能会成功。
 * <p>因为它支持跨多种锁定模式的协调使用，所以此类不直接实现{@link Lock}或{@link ReadWriteLock}接口。
 * 但是，在仅需要相关功能集的应用程序中，可以查看StampedLock {@link #asReadLock（）}，
 * {@link #asWriteLock（）}或{@link #asReadWriteLock（）}。
 * <p> <b>示例用法。</ b>以下说明了维护简单二维点的类中的一些用法习惯用法。
 * 示例代码说明了一些try / catch约定，即使这里没有严格要求，因为它们的主体中不会发生异常
 *
 *
 *  <pre>{@code
 * class Point {
 *   private double x, y;
 *   private final StampedLock sl = new StampedLock();
 *
 *   void move(double deltaX, double deltaY) { // an exclusively locked method
 *     long stamp = sl.writeLock();
 *     try {
 *       x += deltaX;
 *       y += deltaY;
 *     } finally {
 *       sl.unlockWrite(stamp);
 *     }
 *   }
 *
 *   double distanceFromOrigin() { // A read-only method
 *     long stamp = sl.tryOptimisticRead();
 *     double currentX = x, currentY = y;
 *     if (!sl.validate(stamp)) {
 *        stamp = sl.readLock();
 *        try {
 *          currentX = x;
 *          currentY = y;
 *        } finally {
 *           sl.unlockRead(stamp);
 *        }
 *     }
 *     return Math.sqrt(currentX * currentX + currentY * currentY);
 *   }
 *
 *   void moveIfAtOrigin(double newX, double newY) { // upgrade
 *     // Could instead start with optimistic, not read mode
 *     long stamp = sl.readLock();
 *     try {
 *       while (x == 0.0 && y == 0.0) {
 *         long ws = sl.tryConvertToWriteLock(stamp);
 *         if (ws != 0L) {
 *           stamp = ws;
 *           x = newX;
 *           y = newY;
 *           break;
 *         }
 *         else {
 *           sl.unlockRead(stamp);
 *           stamp = sl.writeLock();
 *         }
 *       }
 *     } finally {
 *       sl.unlock(stamp);
 *     }
 *   }
 * }}</pre>
 *
 * @since 1.8
 * @author Doug Lea
 * public class StampedLock implements java.io.Serializable {}
 */
