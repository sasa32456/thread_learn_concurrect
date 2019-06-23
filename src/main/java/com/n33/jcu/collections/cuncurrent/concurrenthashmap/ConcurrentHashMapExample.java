package com.n33.jcu.collections.cuncurrent.concurrenthashmap;

/**
 *   1.7                              ----- hashEntry
 * ConcurrentHashMap  -----seqments   ----- hashEntry
 *                                    ----- hashEntry
 *
 *                    -----seqments   ----- hashEntry
 *
 *                    .............
 *
 *   1.8重写 没有seqments ，改用数组链表，红黑树等
 *
 *   。。。。。
 *
 * @author N33
 * @date 2019/6/17
 */
public class ConcurrentHashMapExample {
}
/**
 * 一个哈希表，支持检索的完全并发和更新的高预期并发性。该类遵循与{@link java.util.Hashtable}相同的功能规范，
 * 并包含与{@code Hashtable}的每个方法相对应的方法版本。但是，即使所有操作都是线程安全的，但检索操作<em>不</ em>需要锁定，
 * 并且<em>不</ em>支持以阻止所有访问的方式锁定整个表。该类与依赖于其线程安全但不依赖于其同步细节的程序中的{@code Hashtable}完全可互操作。
 *
 * <p>检索操作（包括{@code get}）通常不会阻塞，因此可能与更新操作重叠（包括{@code put}和{@code remove}）。
 * 检索反映了最近<em>已完成</ em>更新操作的结果。 （更正式地说，给定密钥的更新操作带有<em>发生 - 在</ em>关系之前与该密钥的任何（非空）检索报告更新的值。）
 * 对于诸如{@code putAll之类的聚合操作}和{@code clear}，并发检索可能反映只插入或删除一些条目。
 * 类似地，Iterators，Spliterators和Enumerations在迭代器/枚举的创建时或之后的某个时刻返回反映哈希表状态的元素。
 * 他们<em>不</ em>抛出{@link java.util.ConcurrentModificationException ConcurrentModificationException}。
 * 但是，迭代器设计为一次只能由一个线程使用。请记住，包括{@code size}，{@code isEmpty}和{@code containsValue}
 * 在内的聚合状态方法的结果通常仅在地图未在其他线程中进行并发更新时才有用。否则，这些方法的结果反映了可能足以用于监视或估计目的的瞬态，但不适用于程序控制。
 *
 * <p>当碰撞太多时（即，具有不同哈希码的密钥但落入与表大小模数相同的槽中的密钥），表会动态扩展，
 * 并且每个映射保持大约两个箱的预期平均效果（对应）调整大小的0.75负载系数阈值。随着映射的添加和删除，
 * 这个平均值可能会有很大的差异，但总的来说，这维持了哈希表的普遍接受的时间/空间权衡。
 * 但是，调整此大小或任何其他类型的散列表可能是一个相对较慢的操作。在可能的情况下，最好将大小估计值作为可选的{@code initialCapacity}构造函数参数提供。
 * 另一个可选的{@code loadFactor}构造函数参数通过指定在计算给定数量的元素时要分配的空间量时使用的表密度，提供了另一种自定义初始表容量的方法。
 * 此外，为了与此类的先前版本兼容，构造函数可以选择指定预期的{@code concurrencyLevel}作为内部大小调整的附加提示。
 * 请注意，使用具有完全相同的{@code hashCode（）}的许多键是降低任何哈希表性能的可靠方法。
 * 为了改善影响，当密钥为{@link Comparable}时，此类可以使用密钥之间的比较顺序来帮助打破关系。
 *
 * <p>可以创建ConcurrentHashMap的{@link Set}投影（使用{@link #newKeySet（）}或{@link #newKeySet（int）}），
 * 或查看（使用{@link #keySet（Object）当只有感兴趣的键，并且映射的值（可能是暂时的）未使用或者所有映射值都采用相同的映射值时。
 *
 * <p>通过使用{@link java.util.concurrent.atomic.LongAdder}值并通过{@link #computeIfAbsent computeIfAbsent}初始化，
 * 可以将ConcurrentHashMap用作可伸缩频率映射（直方图或多重集的一种形式）。
 * 例如，要向{@code ConcurrentHashMap <String，LongAdder> freqs}添加计数，
 * 您可以使用{@code freqs.computeIfAbsent（k  - > new LongAdder（））。increment（）;}
 *
 * <p>这个类及其视图和迭代器实现了所有的{@link Map}和{@link Iterator}接口的<em>可选</ em>方法。
 *
 * <p>与{@link Hashtable}类似，但与{@link HashMap}不同，此类<em>不</ em>允许{@code null}用作键或值。
 *
 * <p> ConcurrentHashMaps支持一组顺序和并行批量操作，与大多数{@link Stream}方法不同，它们被设计为安全且通常明智地应用，
 * 即使是由其他线程同时更新的映射;例如，在共享注册表中计算值的快照摘要时。
 * 有三种操作，每种操作有四种形式，接受具有键，值，条目和（键，值）参数和/或返回值的函数。
 * 因为ConcurrentHashMap的元素没有以任何特定的方式排序，并且可以在不同的并行执行中以不同的顺序处理，
 * 所提供的函数的正确性不应该依赖于任何排序，或者可能依赖于任何其他可能暂时改变的对象或值。
 * 计算正在进行中;除了forEach动作外，理想情况下应该是无副作用的。
 * {@link java.util.Map.Entry}对象上的批量操作不支持方法{@code setValue}。
 *
 * <ul>
 * <li> forEach：对每个元素执行给定的操作。变量形式在执行操作之前对每个元素应用给定的变换。</ li>
 *
 * <li> search：返回在每个元素上应用给定函数的第一个可用的非null结果;在找到结果时跳过进一步的搜索。</ li>
 *
 * <li> reduce：累积每个元素。提供的简化函数不能依赖于排序（更正式地说，它应该是关联的和可交换的）。有五种变体：
 *
 * <ul>
 *
 * <li>简单减少。 （对于（key，value）函数参数，没有这种方法的形式，因为没有相应的返回类型。）</ li>
 *
 * <li>映射缩减，累积应用于每个元素的给定函数的结果。</ li>
 *
 * <li>使用给定的基值减少标量的双精度，长数和整数。</ li>
 * <p>
 * </ ul>
 * </li>
 * </ul>
 *
 * <p>这些批量操作接受{@code parallelismThreshold}参数。如果估计当前地图大小小于给定阈值，则方法顺序进行。
 * 使用{@code Long.MAX_VALUE}值可以抑制所有并行性。使用{@code 1}的值可以通过划分为足够的子任务来充分利用用于所有并行计算的
 * {@link ForkJoinPool＃commonPool（）}来实现最大并行度。通常，您最初会选择其中一个极端值，然后测量使用中间值的性能，这些值会影响开销与吞吐量之间的差异。
 *
 * <p>批量操作的并发属性遵循ConcurrentHashMap的并发属性：从{@code get（key）}返回的任何非null结果和相关的访问方法都与相关的插入或更新有关。
 * 任何批量操作的结果都反映了这些每元素关系的组成（但不一定是整个地图的原子，除非它以某种方式被称为静止）。
 * 相反，因为映射中的键和值永远不为null，所以null可以作为当前缺少任何结果的可靠原子指示符。
 * 要维护此属性，null将作为所有非标量缩减操作的隐式基础。对于double，long和int版本，基础应该是一个，
 * 当与任何其他值组合时，返回其他值（更正式地说，它应该是减少的标识元素）。最常见的减少具有这些特性;
 * 例如，以基数MAX_VALUE计算基数为0或最小值的和。
 *
 * <p>作为参数提供的搜索和转换函数应该类似地返回null以指示缺少任何结果（在这种情况下不使用它）。
 * 在映射缩减的情况下，这还使转换能够用作过滤器，如果不应该组合元素，则返回null（或者，如果是原始特化，则返回标识基础）。
 * 在搜索或减少操作中使用它们之前，您可以通过在“null表示现在没有任何内容”规则下自己编写复合变换和过滤来创建复合变换和过滤。
 *
 * <p>接受和/或返回Entry参数的方法维护键值关联。例如，当找到具有最大价值的密钥时，它们可能是有用的。请注意，
 * 可以使用{@code new AbstractMap.SimpleEntry（k，v）}提供“plain”Entry参数。
 *
 * <p>批量操作可能会突然完成，抛出在应用函数中遇到的异常。在处理此类异常时请记住，
 * 其他并发执行的函数也可能抛出异常，或者如果没有发生第一个异常，则会这样做。
 *
 * <p>与顺序形式相比，并行加速很常见，但不能保证。如果并行计算的基础工作比计算本身更昂贵，
 * 则涉及小地图上的简短函数的并行操作可能比顺序形式执行得更慢。同样，如果所有处理器忙于执行不相关的任务，
 * 并行化可能不会导致太多实际的并行性。
 *
 * <p>所有任务方法的所有参数都必须为非null。
 *
 * <p>此课程是该课程的成员
 * <a href="{@docRoot}/../technotes/guides/collections/index.html">
 * Java Collections Framework </a>。
 *
 * <pre>
 * @since 1.5
 * @author Doug Lea
 * @param <K> the type of keys maintained by this map
 * @param <V> the type of mapped values
 *           public class ConcurrentHashMap<K,V> extends AbstractMap<K,V>
 *         implements ConcurrentMap<K,V>, Serializable {
 * </pre>
 */
