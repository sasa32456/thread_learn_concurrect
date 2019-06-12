package com.n33.jcu.collections.blocking.synchronousqueue;

import java.util.concurrent.SynchronousQueue;

/**
 * 无容量queue
 * 必须要有人在读才可以插入
 * vice versa反之亦然
 *
 * @author N33
 * @date 2019/6/10
 */
public class SynchronousQueueExample {


    public static  <T> SynchronousQueue<T> create() {
        return new SynchronousQueue<>();
    }
}
/**
* {@linkplain BlockingQueue阻塞队列}，其中每个插入操作必须等待另一个线程执行相应的删除操作，反之亦然。
 * 同步队列没有任何内部容量，甚至没有容量。你不能在同步队列中{@code peek}，因为只有当你试图删除它时才会出现一个元素;
 * 你不能插入一个元素（使用任何方法），除非另一个线程试图删除它;你不能迭代，因为没有什么可以迭代。
 * 队列的<em> head </ em>是第一个排队插入线程尝试添加到队列的元素;如果没有这样排队的线程，则没有可用于删除的元素，
 * {@code poll（）}将返回{@code null}。出于其他{@code Collection}方法的目的（例如{@code contains}），{@code SynchronousQueue}充当空集合。
 * 此队列不允许{@code null}元素。
 *
 * <p>同步队列类似于CSP和Ada中使用的集合点通道。它们非常适用于切换设计，
 * 其中在一个线程中运行的对象必须与在另一个线程中运行的对象同步，以便将其传递给某些信息，事件或任务。
 *
 * <p>此类支持用于订购等待生产者和消费者线程的可选公平策略。默认情况下，不保证此顺序。
 * 但是，使用设置为{@code true}的公平性构造的队列以FIFO顺序授予线程访问权限。
 *
 * <p>该类及其迭代器实现了{@link Collection}和{@link Iterator}接口的所有<em>可选</ em>方法。
 *
 * <p>This class is a member of the
 * <a href="{@docRoot}/../technotes/guides/collections/index.html">
 * Java Collections Framework</a>.
 *
 * <pre>
 * @since 1.5
 * @author Doug Lea and Bill Scherer and Michael Scott
 * @param <E> the type of elements held in this collection
 * public class SynchronousQueue<E> extends AbstractQueue<E>
 *         implements BlockingQueue<E>, java.io.Serializable {
 * </pre>
 *
 */
