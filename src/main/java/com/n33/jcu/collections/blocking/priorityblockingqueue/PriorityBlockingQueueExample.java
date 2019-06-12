package com.n33.jcu.collections.blocking.priorityblockingqueue;

import java.util.Comparator;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * 支持优先级（排序）的队列
 *
 * @author N33
 * @date 2019/6/10
 */
public class PriorityBlockingQueueExample {

    public <T> PriorityBlockingQueue<T> create(int size) {
        return new PriorityBlockingQueue<>(size);
    }

    public <T> PriorityBlockingQueue<T> create(int size, Comparator<T> comparator) {
        return new PriorityBlockingQueue<>(size, comparator);
    }
}
/**
 * 一个无限制的{@linkplain BlockingQueue阻塞队列}，它使用与类{@link PriorityQueue}相同的排序规则，并提供阻塞检索操作。
 * 虽然此队列在逻辑上是无限制的，但由于资源耗尽（导致{@code OutOfMemoryError}），尝试添加可能会失败。此类不允许{@code null}元素。
 * 依赖于{@linkplain Comparable natural ordering}的优先级队列也不允许插入不可比较的对象（这样做导致{@code ClassCastException}）。
 *
 * <p>该类及其迭代器实现了{@link Collection}和{@link Iterator}接口的所有<em>可选</ em>方法。
 * 方法{@link #iterator（）}中提供的迭代器<em> not </ em>保证以任何特定顺序遍历PriorityBlockingQueue的元素。
 * 如果需要有序遍历，请考虑使用{@code Arrays.sort（pq.toArray（））}。
 * 此外，方法{@code drainTo}可用于<em>删除</ em>优先顺序中的部分或全部元素，并将它们放在另一个集合中。
 *
 * <p>此类的操作不保证具有相同优先级的元素的排序。如果需要强制执行排序，则可以定义使用辅助键来断开主要优先级值中的关系的自定义类或比较器。
 * 例如，这是一个将先进先出的打破平局应用于可比元素的类。要使用它，您将插入{@code new FIFOEntry（anEntry）}而不是普通条目对象。
 *
 * <pre> {@code
 * class FIFOEntry<E extends Comparable<? super E>>
 *     implements Comparable<FIFOEntry<E>> {
 *   static final AtomicLong seq = new AtomicLong(0);
 *   final long seqNum;
 *   final E entry;
 *   public FIFOEntry(E entry) {
 *     seqNum = seq.getAndIncrement();
 *     this.entry = entry;
 *   }
 *   public E getEntry() { return entry; }
 *   public int compareTo(FIFOEntry<E> other) {
 *     int res = entry.compareTo(other.entry);
 *     if (res == 0 && other.entry != this.entry)
 *       res = (seqNum < other.seqNum ? -1 : 1);
 *     return res;
 *   }
 * }}</pre>
 *
 * <p>This class is a member of the
 * <a href="{@docRoot}/../technotes/guides/collections/index.html">
 * Java Collections Framework</a>.
 *
 * @author Doug Lea
 * @param <E> the type of elements held in this collection
 *
 * <pre>
 * @SuppressWarnings("unchecked")
 * public class PriorityBlockingQueue<E> extends AbstractQueue<E>
 *         implements BlockingQueue<E>, java.io.Serializable {}
 * </pre>
 * @since 1.5
 */
