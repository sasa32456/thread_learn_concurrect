package com.n33.jcu.collections.cuncurrent.concurrentskiplist;

import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * key - value
 *
 * @author N33
 * @date 2019/6/18
 */
public class ConcurrentSkipListMapExample {

    public static <K, V> ConcurrentSkipListMap<K, V> create() {
        return new ConcurrentSkipListMap<>();
    }
}
/**
 * Navigable 通航
 * 可扩展的并发{@link ConcurrentNavigableMap}实现。地图根据其键的{@linkplain Comparable natural ordering}
 * 或地图创建时提供的{@link Comparator}进行排序，具体取决于使用的构造函数。
 *
 * <p>此类实现<a href="http://en.wikipedia.org/wiki/Skip_list" target="_top"> SkipLists </a>的并发变体，
 * 提供预期的平均<i> log（n ）</ i> {@code containsKey}，{@code get}，{@code put}和{@code remove}操作及其变体的时间成本。
 * 插入，删除，更新和访问操作由多个线程安全地同时执行。
 *
 * <p>迭代器和分裂器是
 * <a href="package-summary.html#Weakly"> <i>弱一致</ i> </a>。
 *
 * <p>升序键有序视图及其迭代器比降序视图快。
 *
 * <p>此类中的方法返回的所有{@code Map.Entry}对及其视图表示生成时映射的快照。他们<em>不</ em>支持{@code Entry.setValue}方法。
 * （但请注意，可以使用{@code put}，{@code putIfAbsent}或{@code replace}更改关联地图中的映射，具体取决于您需要的确切效果。）
 *
 * <p>请注意，与大多数集合不同，{@code size}方法<em>不</ em>是一个恒定时间操作。由于这些映射的异步性质，确定元素的当前数量需要遍历元素，
 * 因此如果在遍历期间修改此集合，则可能会报告不准确的结果。此外，批量操作{@code putAll}，{@code equals}，{@code toArray}，
 * {@code containsValue}和{@code clear} <em>不</ em>保证以原子方式执行。例如，与{@code putAll}操作同时运行的迭代器可能只查看一些添加的元素。
 *
 * <p>该类及其视图和迭代器实现了{@link Map}和{@link Iterator}接口的所有<em>可选</ em>方法。与大多数其他并发集合一样，
 * 此类<em>不</ em>允许使用{@code null}键或值，因为某些空返回值无法与缺少元素进行可靠区分。
 *
 * <p>此类是<a href="{@docRoot}/../technotes/guides/collections/index.html"> Java Collections Framework </a>的成员。
 *
 *
 * <pre>
 * @author Doug Lea
 * @param <K> the type of keys maintained by this map
 * @param <V> the type of mapped values
 * @since 1.6
 * public class ConcurrentSkipListMap<K,V> extends AbstractMap<K,V>
 *         implements ConcurrentNavigableMap<K,V>, Cloneable, Serializable {
 * </pre>
 */
