package com.n33.jcu.collections.blocking.arrayblockingqueue;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * 数组阻塞队列
 * @author N33
 * @date 2019/6/9
 */
public class ArrayBlockingQueueExample {






    /**
     * FIFO (Fist in First out)
     * Once created, the capacity cannot be changed.
     * @param size
     * @param <T>
     * @return
     */
    public <T> ArrayBlockingQueue<T> create(int size) {
        return new ArrayBlockingQueue<>(size);
    }
}
/**
 * 由数组支持的有界{@linkplain BlockingQueue阻塞队列}。此队列对元素FIFO（先进先出）进行排序。队列的<em> head </ em>是队列中最长时间的元素。
 * 队列的<em> tail </ em>是队列中最短时间的元素。新元素插入队列的尾部，队列检索操作获取队列头部的元素。
 *
 * <p>这是一个经典的“有界缓冲区”，其中固定大小的数组包含由生产者插入并由消费者提取的元素。创建后，无法更改容量。
 * 尝试将{@code put}元素放入完整队列将导致操作阻塞;尝试{@code take}空队列中的元素同样会阻塞。
 *
 * <p>此类支持用于订购等待生产者和消费者线程的可选公平策略。默认情况下，不保证此顺序。
 * 但是，使用设置为{@code true}的公平性构造的队列以FIFO顺序授予线程访问权限。公平性通常会降低吞吐量，但会降低可变性并避免饥饿。
 *
 * <p>该类及其迭代器实现了{@link Collection}和{@link Iterator}接口的所有<em>可选</ em>方法。
 *
 * <p>此类是<a href="{@docRoot}/../technotes/guides/collections/index.html"> Java Collections Framework </a>的成员。
 *
 * @author Doug Lea
 * @param <E> the type of elements held in this collection
 * <pre>
 * public class ArrayBlockingQueue<E> extends AbstractQueue<E>
 *         implements BlockingQueue<E>, java.io.Serializable {}
 * </pre>
 * @since 1.5
 */
