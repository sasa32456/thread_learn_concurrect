package com.n33.jcu.collections.blocking.delayqueue;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;

/**
 * 1.The delay queue will ordered by expired time?
 * 2.When poll the empty delay queue will return null? use take?
 * 3.When less the expire time will return quickly?
 * 4.Even though unexpired elements cannot be removed using {@code take} or {@code poll}
 * 5.This queue does not permit null elements.
 * 6.Use iterator can return quickly?
 *
 * NOTICE: The DelayQueue element must implement the {@link java.util.concurrent.Delayed}
 * The DelayQueue is a unbounded queue
 *
 * @author N33
 * @date 2019/6/10
 */
public class DelayQueueExample {

    public static  <T extends Delayed> DelayQueue<T> create() {
        return new DelayQueue<>();
    }

}
/**
* {@code Delayed}元素的无界{@linkplain BlockingQueue阻塞队列}，其中元素只能在其延迟到期时获取。
 * 队列的<em> head </ em>是{@code Delayed}元素，其延迟在过去最远。如果没有延迟过期，则没有头，{@code poll}将返回{@code null}。
 * 当元素的{@code getDelay（TimeUnit.NANOSECONDS）}方法返回小于或等于零的值时，会发生过期。
 * 即使使用{@code take}或{@code poll}无法删除未到期的元素，它们也会被视为普通元素。
 * 例如，{@code size}方法返回已过期和未过期元素的计数。此队列不允许null元素。
 *
 * <p>该类及其迭代器实现了{@link Collection}和{@link Iterator}接口的所有<em>可选</ em>方法。
 * 方法{@link #iterator（）}中提供的迭代器<em> not </ em>保证以任何特定顺序遍历DelayQueue的元素。
 *
 * <p>This class is a member of the
 * <a href="{@docRoot}/../technotes/guides/collections/index.html">
 * Java Collections Framework</a>.
 *
 * <pre>
 * @since 1.5
 * @author Doug Lea
 * @param <E> the type of elements held in this collection
 * public class DelayQueue<E extends Delayed> extends AbstractQueue<E>
 *         implements BlockingQueue<E> {}
 * </pre>
 */
