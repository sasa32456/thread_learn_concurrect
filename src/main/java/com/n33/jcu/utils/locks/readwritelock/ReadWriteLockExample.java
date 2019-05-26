package com.n33.jcu.utils.locks.readwritelock;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 读写锁
 *
 * @author N33
 * @date 2019/5/26
 */
public class ReadWriteLockExample {

    /**
     * 一个锁生成两个子锁
     */
    private final static ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock(true);
    private final static Lock readLock = readWriteLock.readLock();
    private final static Lock writeLock = readWriteLock.writeLock();




    private final static List<Long> data = new ArrayList<>();

    public static void main(String[] args) throws InterruptedException {

        final Thread thread1 = new Thread(ReadWriteLockExample::write);
        thread1.start();

        TimeUnit.SECONDS.sleep(1);

        final Thread thread2 = new Thread(ReadWriteLockExample::read);
        thread2.start();
        final Thread thread3 = new Thread(ReadWriteLockExample::read);
        thread3.start();


    }

    private static void write() {
        try {
            writeLock.lock();
            data.add(System.currentTimeMillis());
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            writeLock.unlock();
        }
    }

    private static void read() {
        try {
            readLock.lock();
            data.forEach(System.out::println);
            TimeUnit.SECONDS.sleep(1);
            System.out.println(Thread.currentThread().getName() + "=============================");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            readLock.unlock();
        }
    }


}
/**
 * {@link ReadWriteLock}的实现支持与{@link ReentrantLock}类似的语义。
 * <p>此类具有以下属性：
 * <ul>
 * <li>
 * <b>采集顺序</ b>
 * <p>此类不会强制执行锁定访问的读取器或编写器首选项顺序。
 * 但是，它确实支持可选的<em>公平性</ em>策略。
 * <dl> <dt> <b> <i>非公平模式（默认）</ i> </ b>
 * <dd>当构造为非公平（默认）时，读写的输入顺序锁定未指定，受限于重入约束。
 * 持续争用的非公平锁定可能无限期地推迟一个或多个读取器或写入器线程，但通常具有比公平锁定更高的吞吐量。
 * <dt> <b> <i>公平模式</ i> </ b> <dd>当构建为公平时，线程使用近似到达顺序策略争用进入。
 * 释放当前保持的锁定时，将为最长等待的单个写入器线程分配写入锁定，
 * 或者如果有一组读取器线程等待的时间超过所有等待的写入器线程，则将为该组分配读取锁定。
 * <p>如果保持写锁定或者有等待的写入程序线程，则尝试获取公平读锁定（非重复）的线程将阻塞。
 * 在最旧的当前等待的写入器线程获取并释放写锁定之前，线程将不会获取读锁定。当然，如果等待的写入者放弃其等待，
 * 将一个或多个读取器线程作为队列中最长的服务器并且写锁定空闲，那么将为这些读取器分配读锁定。
 * <p>尝试获取公平写入锁（非重复性）的线程将阻塞，除非读取锁定和写入锁定都是空闲的（这意味着没有等待的线程）。
 * （请注意，非阻塞{@link ReadLock＃tryLock（）}和{@link WriteLock＃tryLock（）}方法不遵循此公平设置，并且如果可能，
 * 将立即获取锁定，无论等待线程如何。） <p> </ dl> <li> <b>重入</ b> <p>
 * 此锁允许读者和作者以{@link ReentrantLock}的方式重新获取读或写锁。在写入线程持有的所有写锁定都被释放之前，
 * 不允许使用非重入读取器。 <p>此外，编写器可以获取读锁定，但反之亦然。在其他应用程序中，
 * 在调用期间保持写锁定或在读取锁定下执行读取的方法的回调时，重入可能很有用。
 * 如果读者试图获取写锁定，它将永远不会成功。 <li> <b>锁定降级</ b> <p>通过获取写锁定，
 * 然后读取锁定然后释放写入锁定，重入也允许从写入锁定降级到读取锁定。
 * 但是，从读锁定升级到写锁定<b>不可能</ b>。
 * <li> <b>锁定获取中断</ b> <p>读取锁定和写入锁定均支持锁定获取期间的中断。
 * <li> <b> {@link Condition}支持</ b> <p>写锁定提供{@link Condition}实现，
 * 其行为方式与写锁相同，如{@link Condition {@link ReentrantLock＃newCondition}为{@link ReentrantLock}提供的实现。
 * 当然，{@link Condition}只能与写锁一起使用。
 * <p>读取锁不支持{@link Condition}和{@code readLock（）。newCondition（）}抛出{@code UnsupportedOperationException}。
 * <li> <b> Instrumentation </ b> <p>此类支持确定锁是保持还是争用的方法。这些方法用于监视系统状态，而不是用于同步控制。
 * </ ul> <p>此类的序列化行为与内置锁的行为方式相同：反序列化锁处于解锁状态，无论序列化时的状态如何。 <p> <b>示例用法</ b>。
 * 下面是一个代码草图，展示了如何在更新缓存后执行锁定降级（在以非嵌套方式处理多个锁时，异常处理尤其棘手）：
 *
 * <pre> {@code
 * class CachedData {
 *   Object data;
 *   volatile boolean cacheValid;
 *   final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
 *
 *   void processCachedData() {
 *     rwl.readLock().lock();
 *     if (!cacheValid) {
 *       // Must release read lock before acquiring write lock
 *       rwl.readLock().unlock();
 *       rwl.writeLock().lock();
 *       try {
 *         // Recheck state because another thread might have
 *         // acquired write lock and changed state before we did.
 *         if (!cacheValid) {
 *           data = ...
 *           cacheValid = true;
 *         }
 *         // Downgrade by acquiring read lock before releasing write lock
 *         rwl.readLock().lock();
 *       } finally {
 *         rwl.writeLock().unlock(); // Unlock write, still hold read
 *       }
 *     }
 *
 *     try {
 *       use(data);
 *     } finally {
 *       rwl.readLock().unlock();
 *     }
 *   }
 * }}</pre>
 * <p>
 * ReentrantReadWriteLocks可用于在某些类型的集合的某些用途中提高并发性。
 * 这通常是值得的，只有当预期集合很大时，由更多的读取器线程访问而不是编写器线程，
 * 并且需要具有超过同步开销的开销的操作。
 * 例如，这是一个使用TreeMap的类，该类预计很大并且可以同时访问。
 *
 * <pre> {@code
 * class RWDictionary {
 *   private final Map<String, Data> m = new TreeMap<String, Data>();
 *   private final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
 *   private final Lock r = rwl.readLock();
 *   private final Lock w = rwl.writeLock();
 *
 *   public Data get(String key) {
 *     r.lock();
 *     try { return m.get(key); }
 *     finally { r.unlock(); }
 *   }
 *   public String[] allKeys() {
 *     r.lock();
 *     try { return m.keySet().toArray(); }
 *     finally { r.unlock(); }
 *   }
 *   public Data put(String key, Data value) {
 *     w.lock();
 *     try { return m.put(key, value); }
 *     finally { w.unlock(); }
 *   }
 *   public void clear() {
 *     w.lock();
 *     try { m.clear(); }
 *     finally { w.unlock(); }
 *   }
 * }}</pre>
 *
 * <h3>实施说明</h3>
 * <p>此锁最多支持65535个递归写锁和65535个读锁。尝试超出这些限制会导致锁定方法引发{@link Error}。
 *
 * @author Doug Lea
 * <p>
 * <p>
 * public class ReentrantReadWriteLock
 * implements ReadWriteLock, java.io.Serializable {}
 * @since 1.5
 */
