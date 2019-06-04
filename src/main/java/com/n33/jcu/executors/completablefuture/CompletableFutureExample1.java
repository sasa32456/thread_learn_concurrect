package com.n33.jcu.executors.completablefuture;

import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 1.8  CompletableFuture 异步回调
 * (TMD当看小说呢)
 *
 * @author N33
 * @date 2019/6/4
 */
public class CompletableFutureExample1 {


    public static void main(String[] args) throws InterruptedException {


        IntStream.range(0, 10).boxed().forEach(i -> CompletableFuture.supplyAsync(CompletableFutureExample1::get)
                .thenAccept(CompletableFutureExample1::display)
                .whenComplete((aVoid, throwable) -> System.out.println(i+" Done"))
        );

        Thread.currentThread().join();


    }


    /**
     * 阻塞的等待输出
     *
     * @throws InterruptedException
     */
    private static void doBlockWork() throws InterruptedException {
        final ExecutorService executorService = Executors.newFixedThreadPool(10);

        final List<Callable<Integer>> tasks = IntStream.range(0, 10).boxed().map(i -> (Callable<Integer>) () -> get()).collect(Collectors.toList());

        executorService.invokeAll(tasks).stream().map(future -> {
            try {
                return future.get();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).parallel().forEach(CompletableFutureExample1::display);
    }


    private static void display(int data) {
        int value = ThreadLocalRandom.current().nextInt(20);
        try {
            System.out.println(Thread.currentThread().getName() + " display will be sleep " + value);
            TimeUnit.SECONDS.sleep(value);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + " display execute done " + data);

    }


    private static int get() {
        int value = ThreadLocalRandom.current().nextInt(20);
        try {
            System.out.println(Thread.currentThread().getName() + " get will be sleep " + value);
            TimeUnit.SECONDS.sleep(value);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + " get execute done " + value);

        return value;
    }


    private static void testCompletable() throws InterruptedException {
        /**
         *
         * main结束就结束,因为被设置为守护线程
         *  <pre>
         * 从ForkJoinWorkerThread构造函数回调以建立并记录其WorkQueue。
         *  ForkJoinWorkerThread:
         *
         *  Creates a ForkJoinWorkerThread operating in the given pool.
         *
         *
         *  ForkJoinPool:
         *     protected ForkJoinWorkerThread(ForkJoinPool pool) {
         *         // Use a placeholder until a useful name can be set in registerWorker
         *          super("aForkJoinWorkerThread");
         *          this.pool = pool;
         *          this.workQueue = pool.registerWorker(this);
         *          }
         *
         *
         *     final WorkQueue registerWorker(ForkJoinWorkerThread wt) {
         *             UncaughtExceptionHandler handler;
         *             wt.setDaemon(true);   ***********************
         *
         *             .........................
         * </pre>
         */
        final CompletableFuture<Void> voidCompletableFuture = CompletableFuture.runAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).whenComplete((aVoid, throwable) -> System.out.println("DONE"));

        System.out.println("================= i am not blocked =================");

        //阻塞main
        Thread.currentThread().join();
    }


    private static void noFutureEndWithFunction() {
        final ExecutorService executorService = Executors.newFixedThreadPool(10);

        //异步调用
        final Future<?> future = executorService.submit(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        //blocked
        while (!future.isDone()) {

        }

        System.out.println("DONE");
    }

}
/**
 * 可以显式完成的{@link Future}（设置其值和状态），并可以用作{@link CompletionStage}，支持在完成时触发的依赖函数和操作。
 *
 * <p>当两个或多个线程尝试时
 * {@link #complete complete}，
 * {@link #completeExceptionally completeExceptionally}，或
 * {@link #cancel取消}
 * 一个CompletableFuture，其中只有一个成功。
 *
 * <p>除了直接操作状态和结果的这些和相关方法之外，CompletableFuture还使用以下策略实现接口{@link CompletionStage}：<ul>
 *
 * <li>为<em>非异步</ em>方法的依赖完成提供的操作可以由完成当前CompletableFuture的线程执行，也可以由完成方法的任何其他调用者执行。</ li>
 *
 * <li>没有显式Executor参数的所有<em> async </ em>方法都是使用{@link ForkJoinPool＃commonPool（）}执行的（除非它不支持至少两个并行级别，
 * 在这种情况下，创建一个新线程来运行每个任务）。为了简化监视，调试和跟踪，
 * 所有生成的异步任务都是标记接口{@link AsynchronousCompletionTask}的实例。 </ LI>
 *
 * <li>所有CompletionStage方法都是独立于其他公共方法实现的，因此一个方法的行为不会受到子类中其他方法的覆盖的影响。 </ li> </ ul>
 *
 * <p> CompletableFuture还使用以下内容实现{@link Future}
 * 政策：<ul>
 *
 * <li>由于（与{@link FutureTask}不同）此类无法直接控制导致其完成的计算，因此取消仅被视为异常完成的另一种形式。
 * 方法{@link #cancel cancel}与{@code completeExceptionally（new CancellationException（））}具有相同的效果。
 * 方法{@link #isCompletedExceptionally}可用于确定CompletableFuture是否以任何特殊方式完成。</ li>
 *
 * <li>如果使用CompletionException完成异常，方法{@link #get（）}和{@link #get（long，TimeUnit）}
 * 会抛出{@link ExecutionException}，其原因与相应的相同CompletionException。为了简化大多数上下文中的使用，
 * 此类还定义了方法{@link #join（）}和{@link #getNow}，它们在这些情况下直接抛出CompletionException。</ li> </ ul>
 *
 * @author Doug Lea
 * @since 1.8
 * <p>
 * <p>
 * 公共类CompletableFuture <T>实现Future <T>，CompletionStage <T> {}
 * <p>
 * 概述：
 * <p>
 * CompletableFuture可能具有相关的完成操作，收集在链接堆栈中。它通过CASing结果字段以原子方式完成，
 * 然后弹出并运行这些操作。这适用于正常与异常结果，同步与异步动作，二进制触发器和各种形式的完成。
 * <p>
 * 字段结果的非零值（通过CAS设置）表示已完成。 AltResult用于将null作为结果，
 * 以及保存异常。使用单个字段可以使检测和触发变得简单。编码和解码很简单，但会增加陷阱并将异常与目标相关联。
 * 较小的简化依赖于（静态）NIL（to box null results）是唯一具有null异常字段的AltResult，因此我们通常不需要进行显式比较。
 * 即使某些泛型类型转换未经检查（请参阅SuppressWarnings注释），即使经过检查，它们也是合适的。
 * <p>
 * 依赖操作由作为Treiber堆栈链接的Completion对象表示，该堆栈由字段“stack”为首。
 * 每种动作都有完成类，分为单输入（UniCompletion），双输入（BiCompletion），
 * 投影（BiCompletions使用两个输入中的两个（不是两个）），共享（CoCompletion，由两个中的第二个使用）来源），
 * 零输入源动作，以及解锁服务员的信号员。类完成扩展了ForkJoinTask以启用异步执行（不添加空间开销，因为我们利用其“标记”方法来维护声明）。
 * 它也被声明为Runnable以允许使用任意执行程序。
 * <p>
 * 对每种CompletionStage的支持依赖于一个单独的类，以及两个CompletableFuture方法：
 * <p>
 * *名称为X的完成类，对应于函数，前缀为“Uni”，“Bi”或“Or”。每个类都包含源（s），
 * 操作和依赖项的字段。它们非常相似，仅与底层功能形式有所不同。我们这样做是为了让用户在常见用法中不会遇到适配器层。
 * 我们还包括与用户方法不对应的“Relay”类/方法;他们将结果从一个阶段复制到另一个阶段
 * <p>
 * * Boolean CompletableFuture方法x（...）（例如uniApply）获取检查操作是否可触发所需的所有参数，
 * 然后运行操作或通过执行其Completion参数（如果存在）来安排其异步执行。如果已知完成，则该方法返回true。
 * <p>
 * *完成方法tryFire（int mode）使用其保持的参数调用关联的x方法，并在成功时清除。
 * mode参数允许tryFire被调用两次（SYNC，然后是ASYNC）;第一个在安排执行时屏蔽和捕获异常，第二个在从任务调用时触发异常。
 * （一些类不使用异步，因此采用略有不同的形式。）如果另一个线程声明了，则claim（）回调会抑制函数调用。
 * <p>
 * * CompletableFuture方法xStage（...）从CompletableFuture x的公共阶段方法调用。
 * 它会筛选用户参数并调用和/或创建舞台对象。如果不是异步并且x已经完成，则立即执行操作。
 * 否则，创建完成c，推送到x的堆栈（除非完成），并通过c.tryFire启动或触发。如果x在推动时完成，这也包括可能的比赛。
 * 具有两个输入的类（例如BiApply）在推动动作时处理两者之间的比赛。第二个完成是CoCompletion指向第一个，
 * 共享，以便最多只执行一个动作。多重方法allOf和anyOf成对地形成完成树。
 * <p>
 * 请注意，方法的泛型类型参数根据“this”是源，依赖还是完成而有所不同。
 * <p>
 * 方法postComplete在完成时被调用，除非保证目标不可观察（即，尚未返回或链接）。
 * 多个线程可以调用postComplete，它会自动弹出每个依赖操作，并尝试在NESTED模式下通过方法tryFire触发它。
 * 触发可以递归传播，因此NESTED模式返回其完成的依赖（如果存在）以供其调用者进一步处理（请参阅postFire方法）。
 * <p>
 * 阻塞方法get（）和join（）依赖于唤醒等待线程的Signaller Completions。
 * 这些机制类似于FutureTask，Phaser和SynchronousQueue中使用的Treiber堆栈等待节点。有关算法详细信息，请参阅其内部文档。
 * <p>
 * 如果没有预防措施，CompletableFutures将容易进行垃圾堆积，因为完成链的堆积，
 * 每个都指向其源。所以我们尽快使字段无效（特别参见方法Completion.detach）。
 * 无论如何，筛选检查无害地忽略了在使用线程将字段置零的比赛期间可能获得的空参数。
 * 我们还尝试从可能永远不会弹出的堆栈中取消链接已触发的完成（请参阅方法postFire）。
 * 完成字段不需要声明为final或volatile，因为它们仅在安全发布时对其他线程可见。
 * <p>
 * <p>
 * <p>
 * <p>
 * <p>
 * <p>
 * ============================================================================
 * <p>
 * <p>
 * <p>
 * <p>
 * <p>
 * <p>
 * <p>
 * <p>
 * <p>
 * <p>
 * <p>
 * <p>
 * <p>
 * 用于运行{@link ForkJoinTask}的{@link ExecutorService}。
 * {@code ForkJoinPool}提供了来自非{@code ForkJoinTask}客户端以及管理和监控操作的提交的入口点。
 *
 * <p> {@code ForkJoinPool}与其他类型的{@link ExecutorService}不同，主要是因为使用<em>工作窃取</ em>：
 * 池中的所有线程都试图查找并执行提交给它的任务池和/或由其他活动任务创建（如果不存在则最终阻止等待工作）。
 * 这可以在大多数任务产生其他子任务时实现高效处理（与大多数{@code ForkJoinTask}一样），以及从外部客户端向池提交许多小任务时。
 * 特别是在构造函数中将<em> asyncMode </ em>设置为true时，{@code ForkJoinPool}也可能适用于从未加入的事件样式任务。
 *
 * <p>静态{@link #commonPool（）}可用，适用于大多数应用程序。
 * 公共池由未显式提交到指定池的任何ForkJoinTask使用。使用公共池通常会减少资源使用
 * （其线程在不使用期间缓慢回收，并在后续使用时恢复）。
 *
 * <p>对于需要单独或自定义池的应用程序，可以使用给定的目标并行度级别构建{@code ForkJoinPool};默认情况下，
 * 等于可用处理器的数量。池尝试通过动态添加，挂起或恢复内部工作线程来维护足够的活动（或可用）线程，
 * 即使某些任务停止等待加入其他任务也是如此。但是，面对阻塞的I / O或其他非托管同步，不能保证这样的调整。
 * 嵌套的{@link ManagedBlocker}接口可以扩展所容纳的同步类型。
 *
 * <p>除了执行和生命周期控制方法之外，此类还提供状态检查方法（例如{@link #getStealCount}），旨在帮助开发，
 * 调优和监视fork / join应用程序。此外，方法{@link #toString}以方便的形式返回池状态的指示以进行非正式监视。
 *
 * <p>与其他ExecutorServices的情况一样，下表总结了三种主要的任务执行方法。
 * 这些主要用于当前池中尚未参与fork / join计算的客户端。这些方法的主要形式接受{@code ForkJoinTask}的实例，
 * 但重载的表单也允许混合执行普通的{@code Runnable}  - 或基于{@code Callable}的活动。
 * 但是，已经在池中执行的任务通常应该使用表中列出的计算内形式，
 * 除非使用通常不连接的异步事件样式任务，在这种情况下，方法选择之间几乎没有区别。
 * <p>
 * <table BORDER CELLPADDING = 3 CELLSPACING = 1>
 * <caption>任务执行方法摘要</ caption>
 * <tr>
 * <td> </ td>
 * <td ALIGN = CENTER> <b>来自非分叉/加入客户的呼叫</ b> </ td>
 * <td ALIGN = CENTER> <b>从fork / join计算中调用</ b> </ td>
 * </ tr>
 * <tr>
 * <td> <b>安排异步执行</ b> </ td>
 * <td> {@link #execute（ForkJoinTask）} </ td>
 * <td> {@link ForkJoinTask＃fork} </ td>
 * </ tr>
 * <tr>
 * <td> <b>等待并获得结果</ b> </ td>
 * <td> {@link #invoke（ForkJoinTask）} </ td>
 * <td> {@link ForkJoinTask＃invoke} </ td>
 * </ tr>
 * <tr>
 * <td> <b>安排执行并获得未来</ b> </ td>
 * <td> {@link #submit（ForkJoinTask）} </ td>
 * <td> {@link ForkJoinTask＃fork}（ForkJoinTasks <em> </ em>期货）</ td>
 * </ tr>
 * </ table>
 *
 * <p>默认情况下，公共池默认使用默认参数构建，但可以通过设置三来控制这些参数
 * {@linkplain System＃getProperty系统属性}：
 * <ul>
 * <li> {@code java.util.concurrent.ForkJoinPool.common.parallelism}
 * - 并行度级别，非负整数
 * <li> {@code java.util.concurrent.ForkJoinPool.common.threadFactory}
 * -  {@link ForkJoinWorkerThreadFactory}的类名
 * <li> {@code java.util.concurrent.ForkJoinPool.common.exceptionHandler}
 * -  {@link UncaughtExceptionHandler}的类名
 * </ ul>
 * 如果存在{@link SecurityManager}且未指定工厂，则默认池使用工厂提供未启用{@link权限}的线程。
 * 系统类加载器用于加载这些类。如果在建立这些设置时出现任何错误，则使用默认参数。
 * 通过将parallelism属性设置为零和/或使用可能返回{@code null}的工厂，可以禁用或限制公共池中线程的使用。
 * 但是，这样做可能会导致永远不会执行未连接的任务。
 *
 * <p> <b>实施说明</ b>：此实现将最大运行线程数限制为32767.尝试创建大于最大数量的池会导致{@code IllegalArgumentException}。
 *
 * <p>此实现仅在池关闭或内部资源耗尽时拒绝提交的任务（即抛出{@link RejectedExecutionException}）。
 * <pre>
 * @since 1.7
 * @author Doug Lea
 * @sun.misc.Contended
 * public class ForkJoinPool extends AbstractExecutorService {}
 * </pre>
 * <p>
 * <p>
 * <p>
 * <p>
 * <p>
 * <p>
 * <p>
 * <p>
 * <p>
 * <p>
 * <p>
 * <p>
 * <p>
 * <p>
 * <p>
 * <p>
 * <p>
 * <p>
 * ===============================================================================
 * <p>
 * <p>
 * <p>
 * <p>
 * <p>
 * <p>
 * <p>
 * <p>
 * <p>
 * <p>
 * <p>
 * <p>
 * <p>
 * <p>
 * <p>
 * <p>
 * <p>
 * <p>
 * 实施概述
 * <p>
 * 此类及其嵌套类为一组工作线程提供主要功能和控制：来自非FJ线程的提交进入提交队列。工作人员承担这些任务并通常将其分成可能被其他工人窃取的子任务。首选规则优先处理从其自己的队列（LIFO或FIFO，取决于模式）处理任务，然后处理其他队列中任务的随机FIFO窃取。该框架开始作为使用工作窃取支持树结构并行性的工具。随着时间的推移，其可扩展性优势导致扩展和更改，以更好地支持更多样化的使用环境。因为大多数内部方法和嵌套类是相互关联的，所以它们的主要原理和描述在这里给出;单个方法和嵌套类仅包含有关详细信息的简短注释。
 * <p>
 * WorkQueues
 * ==========
 * <p>
 * 大多数操作发生在工作窃取队列中（在嵌套类WorkQueue中）。这些特殊形式的Deques只支持四种可能的结束操作中的三种
 * - 推送，弹出和轮询（也称为窃取），在进一步的限制下，push和pop仅从拥有的线程调用（或者，作为扩展）这里，在锁定下），而poll可以从其他线程调用。
 * （如果你不熟悉它们，你可能想阅读Herlihy和Shavit的书“多处理器编程的艺术”，第16章在继续之前更详细地描述这些。）
 * 主要的工作窃取队列设计大致类似于Chase和Lev撰写的“动态循环工作窃取Deque”，SPAA 2005（http://research.sun.com/scalable/pubs/index.html）
 * 和Michael，Saraswat和Vechev的“幂等工作窃取”，PPoPP 2009年（http://portal.acm.org/citation.cfm?id=1504186）。
 * 主要差异最终源于GC要求，我们尽快取消了使用插槽，即使在生成大量任务的程序中也尽可能保持足够的占地面积。为了实现这一点，
 * 我们将CAS仲裁弹出与轮询（窃取）从索引（“基础”和“顶部”）转移到插槽本身。
 * <p>
 * 然后添加任务采用经典数组推送（任务）的形式：
 * q.array[q.top] = task; ++q.top;
 * <p>
 * （实际代码需要对数组进行空检查和大小检查，正确阻止访问，并可能发信号等待工作人员开始扫描 - 见下文。）
 * 成功的pop和poll主要需要一个来自非槽的CAS -null为null。
 * <p>
 * The pop operation (always performed by owner) is:
 * if ((base != top) and
 * (the task at top slot is not null) and
 * (CAS slot to null))
 * decrement top and return task;
 * <p>
 * And the poll operation (usually by a stealer) is
 * if ((base != top) and
 * (the task at base slot is not null) and
 * (base has not changed) and
 * (CAS slot to null))
 * increment base and return task;
 * <p>
 * 因为我们依赖于引用的CAS，所以我们不需要基础或顶部的标记位。它们是在任何基于圆形阵列的队列中使用的简单整数（参见例如ArrayDeque）。
 * 对索引的更新保证top == base意味着队列是空的，但是当推送，弹出或轮询没有完全提交时，可能会使队列显得非空。
 * （方法isEmpty（）检查部分完成删除最后一个元素的情况。）因此，单独考虑的轮询操作不是等待的。
 * 一个小偷无法成功继续，直到另一个小偷（或者，如果先前为空，推动）完成。但是，总的来说，我们至少确保概率无阻塞。
 * 如果尝试窃取失败，小偷总是会选择一个不同的随机受害者目标来尝试下一步。因此，为了让一个小偷进步，任何正在进行的轮询或任何空队列的新推送都可以完成。 （这就是为什么我们通常使用方法pollAt及其在明显基本索引上尝试一次的变体，否则考虑替代操作，而不是重试的方法轮询。）
 * <p>
 * 这种方法还支持用户模式，其中本地任务处理是FIFO，而不是LIFO顺序，只需使用poll而不是pop。
 * 这在从不加入任务的消息传递框架中非常有用。然而，两种模式都不考虑亲和力，负载，缓存本地等，
 * 因此很少在给定的机器上提供最佳性能，但通过对这些因素求平均可以提供良好的吞吐量。此外，即使我们确实试图使用这些信息，
 * 我们通常也没有开发它的基础。例如，一些任务集从缓存亲和力中获益，但其他任务受到缓存污染影响的伤害。
 * 此外，即使需要扫描，长期吞吐量通常最好使用随机选择而不是定向选择策略，因此在适用的情况下使用足够质量的廉价随机化。
 * 各种Marsaglia XorShifts（一些具有不同的移位常数）在使用点处内联。
 * <p>
 * WorkQueues也以类似的方式用于提交到池的任务。我们不能将这些任务混合在工人使用的相同队列中。
 * 相反，我们使用散列形式随机将提交队列与提交线程相关联。 ThreadLocalRandom探测器值用作选择现有队列的哈希码，
 * 并且可以在与其他提交者争用时随机重新定位。本质上，提交者的行为类似于工作者，除了他们被限制执行他们提交的本地任务
 * （或者在CountedCompleters的情况下，其他人具有相同的根任务）。在共享模式下插入任务需要锁定（主要是为了在调整大小的情况下保护）
 * ，但我们只使用一个简单的自旋锁（使用字段qlock），因为遇到忙队列的提交者继续尝试或创建其他队列
 * - 他们阻止仅在创建和注册新队列时。此外，“qlock”在关机时饱和为可解锁值（-1）。在成功案例中，解锁仍然可以通过更便宜的“qlock”有序写入来执行，
 * 但在不成功的情况下使用CAS。
 * <p>
 * Management
 * ==========
 * <p>
 * 工作窃取的主要吞吐量优势源于分散控制
 * - 工人大多从自己或彼此接受任务，速度可超过每秒10亿。池本身创建，激活（启用扫描和运行任务），停用，阻止和终止线程，
 * 所有这些都只需要最少的中央信息。我们可以全局跟踪或维护一些属性，因此我们将它们打包成少量变量，
 * 通常保持原子性而不会阻塞或锁定。几乎所有基本上原子控制状态都保存在两个易变量中，这些变量最常被读取（未写入）作为状态和一致性检查。
 * （另外，字段“config”保持不变的配置状态。）
 * <p>
 * 字段“ctl”包含64位，其保存原子决定添加，停用，排队（在事件队列上），出列和/或重新激活工作者所需的信息。为了实现这种打包，
 * 我们将最大并行度限制为（1 << 15）-1（远远超过正常工作范围），以允许id，计数及其否定（用于阈值处理）适合16位子字段。
 * <p>
 * 字段“runState”保存可锁定状态位（STARTED，STOP等），同时保护对workQueues数组的更新。当用作锁时，它通常仅用于少量指令
 * （唯一的例外是一次性数组初始化和不常见的大小调整），因此在最多短暂旋转后几乎总是可用。但要谨慎，在旋转之后，
 * 方法awaitRunStateLock（仅在初始CAS失败时调用），在内置监视器上使用等待/通知机制来阻止何时（很少）需要。
 * 对于高度争用的锁定来说这是一个可怕的想法，但是大多数游泳池在旋转限制之后没有锁定的情况下运行，所以这可以作为更保守的替代方案。
 * 因为我们没有内部Object用作监视器，所以在可用时使用“stealCounter”（一个AtomicLong）（它也必须被懒惰地初始化;请参阅externalSubmit）。
 * <p>
 * “runState”与“ctl”的用法仅在一种情况下相互作用：决定添加工作线程（请参阅tryAddWorker），在这种情况下，在保持锁定时执行ctl CAS。
 * <p>
 * 录制WorkQueues。 WorkQueues记录在“workQueues”数组中。首次使用时会创建该数组（请参阅externalSubmit）并在必要时进行扩展。
 * 在记录新工作器和未记录终止工作器时对阵列的更新由runState锁相互保护，但该数组可以同时读取并直接访问。
 * 我们还确保数组引用本身的读取永远不会过时。为了简化基于索引的操作，数组大小始终是2的幂，并且所有读取器必须容忍空槽。
 * 工人队列是奇数指数。共享（提交）队列在偶数索引处，最多64个插槽，以限制增长，即使阵列需要扩展以添加更多工作者。
 * 以这种方式将它们组合在一起简化并加速了任务扫描。
 * <p>
 * 所有工作线程创建都是按需创建的，由任务提交，替换已终止的工作人员和/或对被阻止的工作人员的补偿触发。
 * 但是，所有其他支持代码都设置为与其他策略一起使用。为了确保我们不会持有会阻止GC的工作者引用，
 * 所有对workQueues的访问都是通过索引进入workQueues数组（这里是一些凌乱的代码结构的一个来源）。
 * 实质上，workQueues数组充当弱引用机制。因此，例如，ctl的堆栈顶部子字段存储索引，而不是引用。
 * <p>
 * 排队空闲工人。与HPC工作窃取框架不同，我们不能让工作人员在无法立即找到任何工作时无限期地扫描任务，
 * 除非看起来有任务可用，否则我们无法启动/恢复工作人员。另一方面，我们必须在提交或生成新任务时迅速采取行动。在许多用途中，
 * 激活工人的加速时间是整体性能的主要限制因素，在JIT编译和分配的程序启动时更加复杂。所以我们尽可能地简化了这一点。
 * <p>
 * “ctl”字段原子地维护活动和总工作者计数以及放置等待线程的队列，以便它们可以被定位用于信令。
 * 活动计数也起到静止指示符的作用，因此当工作人员认为不再执行任务时会减少。 “队列”实际上是Treiber堆栈的一种形式。
 * 堆栈非常适合以最近使用的顺序激活线程。这样可以提高性能和位置，超过容易发生争用和无法释放工作人员的缺点，除非它是最重要的堆栈。
 * 当他们找不到工作时，我们在推动空闲工作堆栈（由ctl的低32位子字段表示）之后停放/取消停放工作。
 * 顶部堆栈状态保存worker的“scanState”字段的值：其索引和状态，以及除计数子字段（也用作版本标记）之外的版本计数器，
 * 提供针对Treiber堆栈ABA效果的保护。
 * <p>
 * 字段scanState由工作人员和池使用来管理和跟踪工作人员是否处于非活动状态（可能是阻塞等待信号），还是扫描任务（当他们都没有忙于运行任务时）。
 * 当一个worker被停用时，它的scanState字段被设置，并且被阻止执行任务，即使它必须扫描一次以避免排队。
 * 请注意，scanState更新滞后队列CAS版本，因此需要小心使用。排队时，scanState的低16位必须保存其池索引。
 * 所以我们在初始化时将索引放在那里（参见registerWorker），否则将其保存在那里或在必要时恢复它。
 * <p>
 * 内存订购。请参阅Le，Pop，Cohen和Nardelli的“正确和高效的弱记忆模型工作窃取”，
 * PPoPP 2013（http://www.di.ens.fr/~zappa/readings/ppopp13.pdf）进行分析工作窃取算法中的存储器排序要求类似于此处使用的算法。
 * 我们通常需要强于最小排序，因为我们有时必须向工人发出信号，要求像Dekker一样的全围栏以避免丢失信号。
 * 在没有昂贵的过度防护的情况下安排足够的排序需要在支持的表达访问限制的方式之间进行权衡。
 * 从队列和更新ctl状态获取的最核心的操作需要全范围CAS。使用Unsafe提供的挥发性仿真读取数组插槽。
 * 从其他线程访问WorkQueue base，top和array需要对这些读取中的第一个进行易失性加载。
 * 我们使用声明“基本”索引volatile的约定，并始终在其他字段之前读取它。所有者线程必须确保有序更新，
 * 因此写入使用有序内在函数，除非他们可以搭载其他写入。类似的约定和基本原理适用于其他WorkQueue字段（例如“currentSteal”），
 * 这些字段仅由所有者编写但由其他人观察。
 * <p>
 * 创造工人。为了创建一个worker，我们预先增加总计数（用作保留），并尝试通过其工厂构造一个ForkJoinWorkerThread。
 * 在构造时，新线程调用registerWorker，在其中构造WorkQueue并在workQueues数组中分配索引（如果需要，扩展数组）。
 * 然后启动该线程。如果这些步骤之间出现任何异常，或者从工厂返回null，则deregisterWorker会相应地调整计数和记录。
 * 如果返回null，则池继续以少于目标数字的worker运行。如果例外，则异常传播，通常传播给某些外部调用者。
 * 工作程序索引分配避免了在workQueues数组的前面开始顺序打包条目时可能发生的扫描偏差。我们将数组视为一个简单的二次幂哈希表，
 * 根据需要进行扩展。 seedIndex增量确保不会发生冲突，直到需要调整大小或者取消注册和替换worker，然后保持较低的冲突概率。
 * 我们不能在此处使用ThreadLocalRandom.getProbe（）用于类似目的，因为线程尚未启动，但是这样做是为了为现有外部线程创建提交队列。
 * <p>
 * 停用并等待。排队遇到几个内在的种族;最值得注意的是，任务生成线程可能会错过查看（并发出信号）另一个放弃寻找工作但尚未进入等待队列的线程。
 * 当工人找不到要偷的任务时，它会停用并排队。通常，由于GC或OS调度，缺少任务是暂时的。为了减少错误警报停用，扫描程序在扫描期间计算队列状态的校验和。
 * （此处和其他地方使用的稳定性检查是快照技术的概率变体 - 请参阅Herlihy＆Shavit。）工作人员放弃并尝试仅在扫描之后总和稳定后才能停用。
 * 此外，为避免遗漏信号，他们在成功入队后重复此扫描过程，直到再次稳定。在这种状态下，工作者在从队列中释放之前不能执行/运行它看到的任务，
 * 因此工作者本身最终会尝试释放自己或任何后继者（请参阅tryRelease）。否则，在空扫描时，停用的工作程序在阻塞（通过停放）之前使用自适应本地旋转构造
 * （请参阅awaitWork）。注意有关Thread.interrupts周围停车和其他阻塞的不寻常约定：因为中断仅用于警告线程检查终止，无论如何在阻塞时检查终止，
 * 我们在任何调用park之前清除状态（使用Thread.interrupted），以便由于状态是通过用户代码中的其他无关的中断调用设置的，因此park不会立即返回。
 * <p>
 * 信号和激活。仅当看起来至少有一个他们可能能够找到并执行的任务时，才会创建或激活工作人员。在推送（由工作人员或外部提交）到先前（可能）空队列时，
 * 工作人员在空闲时发出信号，或者如果存在的数量少于给定的并行度级别则创建。每当其他线程从队列中删除任务并注意到其他任务时，
 * 这些主要信号也会受到其他人的支持。在大多数平台上，信令（取消停放）开销时间明显很长，并且发信号通知线程和实际进行之间的时间可能非常长，
 * 因此值得尽可能地从关键路径卸载这些延迟。此外，由于非活动工作人员经常重新扫描或旋转而不是阻止，
 * 我们设置并清除WorkQueues的“parker”字段以减少对unpark的不必要调用。 （这需要进行二次重新检查以避免错过信号。）
 * <p>
 * 修剪工人。要在缺少使用期后释放资源，当池静止时开始等待的工作人员将超时并终止（请参阅awaitWork）如果池在IDLE_TIMEOUT期间保持静止，
 * 则随着线程数减少而增加周期，最终删除所有工人。此外，当存在两个以上的备用线程时，多余的线程会立即在下一个静止点终止。
 * （两个填充可以避免滞后现象。）
 * <p>
 * 关机和终止。对shutdownNow的调用调用tryTerminate以原子方式设置runState位。调用线程以及此后终止的所有其他工作者通过设置其（qlock）状态，
 * 取消未处理的任务并唤醒它们来帮助终止其他人，重复这样做直到稳定（但是由于工作人员数量限制的循环） ）。通过检查终止是否应该开始来调用非突然关闭（）
 * 。这主要依赖于保持共识的“ctl”的活动计数位 - 每当静止时，都会从awaitWork调用tryTerminate。但是，外部提交者不参与此共识。
 * 因此，tryTerminate扫描队列（直到稳定）以确保缺少飞行中的提交和工作人员在触发终止的“停止”阶段之前处理它们。
 * （注意：如果在启用关闭时调用了helpQuiescePool，则会发生内在冲突。两者都等待静止，但是在helpQuiescePool完成之前，tryTerminate偏向于不会触发。）
 * <p>
 * <p>
 * Joining Tasks
 * =============
 * <p>
 * 当一名工人等待加入另一名被盗（或永远持有）的任务时，可以采取任何一种行动。因为我们将许多任务复用到工作池上，
 * 所以我们不能让它们阻塞（如在Thread.join中）。我们也不能只是将joiner的运行时堆栈重新分配给另一个并稍后替换它，
 * 这将是一种“延续”，即使可能也不一定是个好主意，因为我们可能需要一个未阻塞的任务和它的继续进展。相反，我们结合了两种策略：
 * <p>
 * 帮助：如果没有发生窃取，安排木匠执行一些将要运行的任务。
 * <p>
 * 补偿：除非已经有足够的活动线程，否则方法tryCompensate（）可以创建或重新激活备用线程，以补偿阻塞的加入者，直到它们解除阻塞。
 * <p>
 * 第三种形式（在tryRemoveAndExec中实现）相当于帮助假设的补偿器：如果我们可以很容易地告诉补偿器的可能动作是窃取并执行正在加入的任务，
 * 则加入线程可以直接执行，而无需补偿线程（虽然以更大的运行时堆栈为代价，但权衡通常是值得的）。
 * <p>
 * ManagedBlocker扩展API无法使用帮助，因此仅依赖于方法awaitBlocker中的补偿。
 * <p>
 * helpStealer中的算法需要一种“线性帮助”的形式。每个工作人员（在字段currentSteal中）记录它从其他工作人员（或提交）中窃取的最新任务。
 * 它还记录（在现场currentJoin中）它当前正在加入的任务。方法helpStealer使用这些标记来尝试找到一个工作人员来帮助
 * （即，从一个任务中窃取一个任务并执行它），这可以加速完成主动连接的任务。
 * 因此，如果待加入的任务没有被盗，则加入者执行将在其自己的本地deque上的任务。这是Wagner＆Calder所描述的方法的保守变体“Leapfrogging：
 * 实现有效期货的便携式技术”SIGPLAN Notices，1993（http://portal.acm.org/citation.cfm?id=155354）。它的不同之处在于：
 * （1）我们只是在窃取时维护员工之间的依赖关系，而不是使用每个任务的簿记。这有时需要对workQueues数组进行线性扫描以找到窃取者，
 * 但通常不会因为窃取者留下提示（可能会陈旧/错误）的位置。这只是一个提示，因为一个工人可能有多次抢断，而提示只记录其中一个（通常是最新的）。
 * 提示隔离成本到需要时，而不是增加每个任务的开销。
 * （2）它是“浅的”，忽略了嵌套和潜在的循环相互窃取。
 * （3）故意racy：field currentJoin仅在积极加入时更新，这意味着我们在长期任务，GC停顿等期间错过链中的链接（这是正常的，
 * 因为在这种情况下阻塞通常是个好主意） 。
 * （4）我们限制了使用校验和找到工作的次数，然后回退到暂停工作人员，必要时将其替换为另一工人。
 * <p>
 * 对CountedCompleters的帮助操作不需要跟踪currentJoins：方法helpComplete接受并执行与等待的任务具有相同根的任何任务
 * （更喜欢本地弹出到非本地轮询）。但是，这仍然需要遍历完整链，因此效率低于使用没有显式连接的CountedCompleters。
 * <p>
 * 补偿的目的不是准确保持在任何给定时间运行的未阻塞线程的目标并行数。此类的某些先前版本对任何阻止的连接使用立即补偿。
 * 但是，在实践中，绝大多数阻塞都是GC和其他JVM或OS活动的短暂副产品，这些活动因更换而变得更糟。
 * 目前，仅在通过检查字段WorkQueue.scanState验证所有声称活动的线程正在处理任务之后才尝试补偿，这消除了大多数误报。
 * 此外，在最常见的情况下，补偿被绕过（容忍更少的线程），这在很少有益的情况下：
 * 当具有空队列（因此​​没有连续任务）的工作程序阻塞连接并且仍然保留足够的线程以确保活跃时。
 * <p>
 * 补偿机制可能有限。 commonPool的边界（请参阅commonMaxSpares）可以更好地使JVM在耗尽资源之前应对编程错误和滥用。
 * 在其他情况下，用户可能会提供限制螺纹结构的工厂。此池中的边界效果（与所有其他池一样）是不精确的。当线程取消注册时，
 * 工作人员总数减少，而不是当它们退出并且JVM和OS回收资源时。因此，同时活动线程的数量可能会暂时超过边界。
 * <p>
 * Common Pool
 * ===========
 * <p>
 * 静态公共池在静态初始化后始终存在。由于它（或任何其他创建的池）不需要使用，我们将初始构造开销和占用空间最小化到大约十二个字段的设置，
 * 没有嵌套分配。在第一次提交到池期间，大多数引导都发生在方法externalSubmit中。
 * <p>
 * 当外部线程提交到公共池时，它们可以在连接时执行子任务处理（请参阅externalHelpComplete和相关方法）。
 * 这种调用者帮助策略使得将公共池并行度级别设置为小于可用核心总数的一个（或更多）是明智的，对于纯调用者运行甚至为零。
 * 我们不需要记录外部提交是否属于公共池 - 如果不是，外部帮助方法会快速返回。否则这些提交者将被阻止等待完成，因此在不适用的情况下额外的努力
 * （通过自由分散的任务状态检查）相当于在ForkJoinTask.join中阻塞之前的有限自旋等待的奇怪形式。
 * <p>
 * 作为托管环境中更合适的默认值，除非被系统属性覆盖，否则当存在SecurityManager时，
 * 我们使用子类InnocuousForkJoinWorkerThread的worker。这些worker没有设置权限，不属于任何用户定义的ThreadGroup，
 * 并且在执行任何顶级任务后擦除所有ThreadLocals（请参阅WorkQueue.runTask）。相关的机制（主要在ForkJoinWorkerThread中）
 * 可能依赖于JVM，并且必须访问特定的Thread类字段才能实现此效果。
 * <p>
 * Style notes
 * ===========
 * <p>
 * 内存排序主要依赖于不安全的内在函数，它还承担着明确执行空值和边界检查的责任，否则JVM会隐式执行这些检查。
 * 这可能是尴尬和丑陋的，但也反映了控制结果的需要，这些不寻常的情况出现在非常简单的代码中，几乎没有不变量。
 * 所以这些显式检查无论如何都会以某种形式存在。所有字段在使用前都会读入本地，如果它们是引用则会进行空值检查。
 * 这通常在方法或块的头部以类似“C”的方式进行列表声明，并在第一次遇到时使用内联赋值。数组边界检查通常通过使用array.length-1进行屏蔽来执行，
 * 该数据依赖于使用正长度创建这些数组的不变量，这本身就是异常检查。几乎所有显式检查都会导致绕过/返回，而不是异常抛出，
 * 因为它们可能由于在关闭期间取消/撤销而合法地出现。
 * <p>
 * ForkJoinPool，ForkJoinWorkerThread和ForkJoinTask类之间有很多表示级耦合。 WorkQueue的字段维护由ForkJoinPool管理的数据结构，
 * 因此可以直接访问。试图减少这一点没有什么意义，因为表示中任何相关的未来变化都需要伴随着算法变化。有几种方法本质上是无用的，
 * 因为它们必须累积一组对局部变量中保存的字段的一致读取。还有其他编码奇怪（包括几个不必要的提升空值检查），
 * 这有助于某些方法即使在解释（未编译）时也能合理地执行。
 * <p>
 * 此文件中的声明顺序是（除了少数例外）：
 * （1）静态效用函数
 * （2）嵌套（静态）类
 * （3）静态字段
 * （4）字段，以及解包其中一些时使用的常量
 * （5）内部控制方法
 * （6）对ForkJoinTask方法的回调和其他支持
 * （7）出口方法
 * （8）以最小依赖顺序初始化静态块的静态块
 */
