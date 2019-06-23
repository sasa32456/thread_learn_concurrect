package com.n33.jcu.collections.cuncurrent.concurrentlinked;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 非空陷阱
 *
 * @author N33
 * @date 2019/6/19
 */
public class ConcurrentLinkedQueueExample {

    public static void main(String[] args) {

        final ConcurrentLinkedQueue<Long> queue = new ConcurrentLinkedQueue<>();

        for (int i = 0; i < 100000; i++) {
            queue.offer(System.nanoTime());
        }

        System.out.println("=========== offer done. ==============");

        long startTime = System.currentTimeMillis();
        //11499 size读数据时间太长
//        while (queue.size() > 0) {
//            queue.poll();
//        }

        //16
        while (!queue.isEmpty()) {
            queue.poll();
        }
        System.out.println(System.currentTimeMillis() - startTime);
    }


    private static void handleText(String s) {
        //垃圾
        if (null == s && "".equals(s)) {

        }

        //好
        if (null == s && s.length() > 0) {

        }
        //同上
        if (null == s && !s.isEmpty()) {

        }
    }


}
/**
 * 基于链接节点的无界线程安全{@linkplain Queue queue}。此队列对元素FIFO（先进先出）进行排序。队列的<em> head </ em>是队列中最长时间的元素。
 * 队列的<em> tail </ em>是队列中最短时间的元素。新元素插入队列的尾部，队列检索操作获取队列头部的元素。
 * 当许多线程共享对公共集合的访问权时，{@code ConcurrentLinkedQueue}是一个合适的选择。
 * 与大多数其他并发集合实现一样，此类不允许使用{@code null}元素。
 *
 * <p>此实现采用了基于<a href =“http://www.cs.rochester.edu/u/michael/PODC96.html”中所述的高效<em>非阻塞</ em>算法。 >
 * Maged M. Michael和Michael L. Scott的简单，快速，实用的非阻塞和阻塞并发队列算法</a>。
 *
 * <p>迭代器<i>弱一致</ i>，在迭代器创建时或之后的某个时刻返回反映队列状态的元素。它们<em>不</ em>抛出{@link java.util.ConcurrentModificationException}，
 * 并且可以与其他操作同时进行。自创建迭代器以来队列中包含的元素将只返回一次。
 *
 * <p>请注意，与大多数集合不同，{@code size}方法<em> NOT </ em>是一个恒定时间操作。由于这些队列的异步性质，
 * 确定元素的当前数量需要遍历元素，因此如果在遍历期间修改此集合，则可能会报告不准确的结果。此外，批量操作{@code addAll}，
 * {@code removeAll}，{@code retainAll}，{@code containsAll}，{@code equals}和{@code toArray} <em>不</ em>保证以原子方式执行。
 * 例如，与{@code addAll}操作同时运行的迭代器可能只查看一些添加的元素。
 *
 * <p>该类及其迭代器实现了{@link Queue}和{@link Iterator}接口的所有<em>可选</ em>方法。
 *
 * <p>内存一致性效果：与其他并发集合一样，在将对象放入{@code ConcurrentLinkedQueue} <a href="package-summary.html#MemoryVisibility"> <i>之前，
 * 线程中的操作发生在以前</ i> </a>在另一个线程中从{@code ConcurrentLinkedQueue}访问或删除该元素之后的操作。
 *
 * <p>此课程是该课程的成员
 * <a href="{@docRoot}/../technotes/guides/collections/index.html">
 * Java Collections Framework </a>。
 *
 * <pre>
 * @since 1.5
 * @author Doug Lea
 * @param <E> the type of elements held in this collection
 * public class ConcurrentLinkedQueue<E> extends AbstractQueue<E>
 *         implements Queue<E>, java.io.Serializable {
 * </pre>
 */
