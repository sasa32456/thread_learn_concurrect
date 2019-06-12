package com.n33.jcu.collections.blocking.linkedblockingqueue;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * 链式queue
 *
 * @author N33
 * @date 2019/6/10
 */
public class LinkedBlockingQueueExample {

    /**
     * 无边界(int MAX)
     *
     * @param <T>
     * @return
     */
    public <T> LinkedBlockingQueue<T> create() {
        return new LinkedBlockingQueue<>();
    }


    /**
     * 有边界,size
     *
     * @param capacity
     * @param <T>
     * @return
     */
    public <T> LinkedBlockingQueue<T> create(int capacity) {
        return new LinkedBlockingQueue<>(capacity);
    }

}
/**
 * 基于链接节点的可选绑定{@linkplain BlockingQueue阻塞队列}。此队列对元素FIFO（先进先出）进行排序。
 * 队列的<em> head </ em>是队列中最长时间的元素。队列的<em> tail </ em>是队列中最短时间的元素。
 * 新元素插入队列的尾部，队列检索操作获取队列头部的元素。
 * 链接队列通常具有比基于阵列的队列更高的吞吐量，但在大多数并发应用程序中具有较低的可预测性能。
 *
 * <p>可选的容量绑定构造函数参数用作防止过多队列扩展的方法。
 * 如果未指定，则容量等于{@link Integer #MAX_VALUE}。每次插入时都会动态创建链接节点，除非这会使队列超出容量。
 *
 * <p>该类及其迭代器实现了{@link Collection}和{@link Iterator}接口的所有<em>可选</ em>方法。
 *
 * <p>This class is a member of the
 * <a href="{@docRoot}/../technotes/guides/collections/index.html">
 * Java Collections Framework</a>.
 *
 * <pre>
 * @since 1.5
 * @author Doug Lea
 * @param <E> the type of elements held in this collection
 * public class LinkedBlockingQueue<E> extends AbstractQueue<E>
 *         implements BlockingQueue<E>, java.io.Serializable {
 * </pre>
 */
