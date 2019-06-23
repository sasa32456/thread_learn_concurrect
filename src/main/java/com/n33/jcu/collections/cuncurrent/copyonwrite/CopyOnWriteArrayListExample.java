package com.n33.jcu.collections.cuncurrent.copyonwrite;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 读归读，读以前
 * 写归写，写新组
 * 以前引用指向新的
 * 从而避开锁
 *
 *
 * 全是读，偶尔写才用不然爆炸
 *
 * @author N33
 * @date 2019/6/20
 */
public class CopyOnWriteArrayListExample {


    /**
     * {@link java.util.concurrent.CopyOnWriteArrayList}
     *
     * @param args
     */
    public static void main(String[] args) {
        CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList<>();


    }
}

/**
* {@link java.util.ArrayList}的线程安全变体，其中所有变异操作（{@code add}，{@code set}等等）都是通过制作底层数组的新副本来实现的。
 *
 * <p>这通常成本太高，但是当遍历操作大大超过突变时，它可能比其他方法更有效率，并且在您不能或不想同步遍历时有用，但需要排除并发线程之间的干扰。
 * “快照”样式迭代器方法在创建迭代器时使用对数组状态的引用。这个数组在迭代器的生命周期中永远不会改变，所以干扰是不可能的，
 * 并且保证迭代器不会抛出{@code ConcurrentModificationException}。自迭代器创建以来，迭代器不会反映列表的添加，删除或更改。
 * 不支持迭代器本身的元素更改操作（{@code remove}，{@ code set}和{@code add}）。这些方法抛出{@code UnsupportedOperationException}。
 *
 * <p>允许使用所有元素，包括{@code null}。
 *
 * <p>内存一致性效果：与其他并发集合一样，在将对象放入{@code CopyOnWriteArrayList} <a href="package-summary.html#MemoryVisibility"> <i>之前，
 * 线程中的操作发生在以前</ i> </a>在另一个线程中从{@code CopyOnWriteArrayList}访问或删除该元素之后的操作。
 *
 * <p>This class is a member of the
 * <a href="{@docRoot}/../technotes/guides/collections/index.html">
 * Java Collections Framework</a>.
 *
 * <pre>
 * @since 1.5
 * @author Doug Lea
 * @param <E> the type of elements held in this collection
 * public class CopyOnWriteArrayList<E>
 *         implements List<E>, RandomAccess, Cloneable, java.io.Serializable {
 * </pre>
 */
