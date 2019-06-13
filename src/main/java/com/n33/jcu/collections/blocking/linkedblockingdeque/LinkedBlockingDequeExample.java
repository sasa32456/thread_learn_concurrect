package com.n33.jcu.collections.blocking.linkedblockingdeque;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * 双向链表
 *
 * @author N33
 * @date 2019/6/12
 */
public class LinkedBlockingDequeExample {

    public static <T> LinkedBlockingDeque<T> create() {
        return new LinkedBlockingDeque<>();
    }
}
/**
 * 基于链接节点的可选有界{@linkplain BlockingDeque blocking deque}。
 *
 * <p>可选的容量绑定构造函数参数用作防止过度扩展的方法。如果未指定，则容量等于{@link Integer #MAX_VALUE}。每次插入时都会动态创建链接节点，除非这会使deque超出容量。
 *
 * <p>大多数操作以恒定时间运行（忽略阻塞时间）。例外情况包括{@link #remove（Object）remove}，{@link #removeFirstOccurrence removeFirstOccurrence}，
 * {@link #removeLastOccurrence removeLastOccurrence}，{@link #contains contains}，{@link #iterator iterator.remove（）}，
 * 以及批量操作，所有操作都以线性时间运行。
 *
 * <p>This class and its iterator implement all of the
 * <em>optional</em> methods of the {@link Collection} and {@link
 * Iterator} interfaces.
 *
 * <p>This class is a member of the
 * <a href="{@docRoot}/../technotes/guides/collections/index.html">
 * Java Collections Framework</a>.
 *
 * <pre>
 * @since 1.6
 * @author Doug Lea
 * @param <E> the type of elements held in this collection
 *
 * public class LinkedBlockingDeque<E>
 *         extends AbstractQueue<E>
 *         implements BlockingDeque<E>, java.io.Serializable {
 * </pre>
 */
