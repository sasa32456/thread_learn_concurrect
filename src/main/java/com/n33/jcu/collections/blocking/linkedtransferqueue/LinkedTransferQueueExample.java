package com.n33.jcu.collections.blocking.linkedtransferqueue;

import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.TransferQueue;

/**
 *
 * An unbound {@link TransferQueue} based on linked nodes.
 * <p>
 * Bounded
 * Producer: when the capacity is full. the producer will blocked,
 * else just only insert the new element into the queue, but the data is consume or not?
 * <p>
 * The TransferQueue is useful in scenario where message passing need to guaranteed
 *
 * 和SynchronousQueue不同，前者没人等着拿就扔
 * 这个是等到别人拿才算拿
 *
 *
 * @author N33
 * @date 2019/6/13
 */
public class LinkedTransferQueueExample {

    public static <T> LinkedTransferQueue<T> create() {
        return new LinkedTransferQueue<>();
    }

}
/**
 * 基于链接节点的无界{@link TransferQueue}。该队列针对任何给定的生产者对元素FIFO（先进先出）进行排序。
 * 队列的<em> head </ em>是队列中某个生产者最长时间的元素。队列的<em> tail </ em>是队列中某个生产者最短时间的元素。
 *
 * <p>请注意，与大多数集合不同，{@code size}方法<em> NOT </ em>是一个恒定时间操作。由于这些队列的异步性质，确定元素的当前数量需要遍历元素，
 * 因此如果在遍历期间修改此集合，则可能会报告不准确的结果。此外，批量操作{@code addAll}，{@code removeAll}，{@code retainAll}，
 * {@code containsAll}，{@code equals}和{@code toArray} <em>不</ em>保证以原子方式执行。
 * 例如，与{@code addAll}操作同时运行的迭代器可能只查看一些添加的元素。
 *
 * <p>该类及其迭代器实现了{@link Collection}和{@link Iterator}接口的所有<em>可选</ em>方法。
 *
 * <p>内存一致性效果：与其他并发集合一样，在将对象放入{@code LinkedTransferQueue} <a href="package-summary.html#MemoryVisibility"> <i>之前，
 * 线程中的操作发生在以前</ i> </a>在另一个线程中从{@code LinkedTransferQueue}访问或删除该元素之后的操作。
 *
 * <p>此类是<a href="{@docRoot}/../technotes/guides/collections/index.html"> Java Collections Framework </a>的成员。
 *
 * <pre>
 * @since 1.7
 * @author Doug Lea
 * @param <E> the type of elements held in this collection
 * public class LinkedTransferQueue<E> extends AbstractQueue<E>
 *         implements TransferQueue<E>, java.io.Serializable {
 * </pre>
 */
