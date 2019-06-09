package com.n33.jcu.collections.blocking.arrayblockingqueue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

/**
 * 数组阻塞队列
 *
 * @author N33
 * @date 2019/6/9
 */
public class ArrayBlockingQueueExampleTest {

    private ArrayBlockingQueueExample example;

    @Before
    public void setUp() {
        example = new ArrayBlockingQueueExample();
    }

    @After
    public void tearDown() {
        example = null;
    }

    @Test
    public void testAddMethodNotExceedCapacity() {
        ArrayBlockingQueue<String> queue = example.create(5);
        assertThat(queue.add("Hello1"), equalTo(true));
        assertThat(queue.add("Hello2"), equalTo(true));
        assertThat(queue.add("Hello3"), equalTo(true));
        assertThat(queue.add("Hello4"), equalTo(true));
        assertThat(queue.add("Hello5"), equalTo(true));
        assertThat(queue.size(), equalTo(5));

    }

    @Test(expected = IllegalStateException.class)
    public void testAddMethodExceedCapacity() {
        ArrayBlockingQueue<String> queue = example.create(5);
        assertThat(queue.add("Hello1"), equalTo(true));
        assertThat(queue.add("Hello2"), equalTo(true));
        assertThat(queue.add("Hello3"), equalTo(true));
        assertThat(queue.add("Hello4"), equalTo(true));
        assertThat(queue.add("Hello5"), equalTo(true));
        assertThat(queue.add("Hello6"), equalTo(true));
        fail("should not process to here.");

    }



    /**
     * Inserts the specified element at the tail of this queue if it is possible to do so immediately without exceeding the queue's capacity,
     * returning {@code true} upon success and {@code false} if this queue is full.  This method is generally preferable to method {@link. #add},
     * which can fail to insert an element only by throwing an exception.
     * 如果可以在不超出队列容量的情况下立即执行此操作，则将指定元素插入此队列的尾部，
     * 成功时返回{@code true}，如果此队列已满，则返回{@code false}。
     * 该方法通常优于方法{@link。 #add}，只能通过抛出异常来插入元素。
     *
     * @throws NullPointerException if the specified element is null
     */
    @Test
    public void testOfferMethodNotExceedCapacity() {
        ArrayBlockingQueue<String> queue = example.create(5);
        assertThat(queue.offer("Hello1"), equalTo(true));
        assertThat(queue.offer("Hello2"), equalTo(true));
        assertThat(queue.offer("Hello3"), equalTo(true));
        assertThat(queue.offer("Hello4"), equalTo(true));
        assertThat(queue.offer("Hello5"), equalTo(true));

        assertThat(queue.size(), equalTo(5));

    }


    /**
     * 超出返回false，不返回异常
     */
    @Test
    public void testOfferMethodExceedCapacity() {
        ArrayBlockingQueue<String> queue = example.create(5);
        assertThat(queue.offer("Hello1"), equalTo(true));
        assertThat(queue.offer("Hello2"), equalTo(true));
        assertThat(queue.offer("Hello3"), equalTo(true));
        assertThat(queue.offer("Hello4"), equalTo(true));
        assertThat(queue.offer("Hello5"), equalTo(true));
        assertThat(queue.offer("Hello6"), equalTo(false));

        assertThat(queue.size(), equalTo(5));

    }


    /**
     * put
     * 放不进去就阻塞
     * 中断异常
     * @throws InterruptedException
     */
    @Test
    public void testPutMethodNotExceedCapacity() throws InterruptedException {
        ArrayBlockingQueue<String> queue = example.create(5);
       queue.put("Hello1");
       queue.put("Hello2");
       queue.put("Hello3");
       queue.put("Hello4");
       queue.put("Hello5");

        assertThat(queue.size(), equalTo(5));

    }


    @Test
    public void testPutMethodExceedCapacity() throws InterruptedException {
        ArrayBlockingQueue<String> queue = example.create(5);

        final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.schedule(() -> {
            try {
                assertThat(queue.take(),equalTo("Hello1"));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, 1, TimeUnit.SECONDS);
        executorService.shutdown();


        queue.put("Hello1");
        queue.put("Hello2");
        queue.put("Hello3");
        queue.put("Hello4");
        queue.put("Hello5");
        queue.put("Hello6");


    }


    /**
     * 拉值(把值取出)
     */
    @Test
    public void testPoll() {
        ArrayBlockingQueue<String> queue = example.create(2);
        queue.add("Hello1");
        queue.add("Hello2");
        ///////////////////////
        assertThat(queue.poll(), equalTo("Hello1"));
        assertThat(queue.poll(), equalTo("Hello2"));
        assertThat(queue.poll(),nullValue());

    }


    /**
     * 拉值（不取出数据）
     */
    @Test
    public void testPeek() {
        ArrayBlockingQueue<String> queue = example.create(2);
        queue.add("Hello1");
        queue.add("Hello2");
        ///////////////////////
        assertThat(queue.peek(), equalTo("Hello1"));
        assertThat(queue.peek(), equalTo("Hello1"));
        assertThat(queue.peek(), equalTo("Hello1"));

    }


    /**
     * 检索但不删除此队列的头部。此方法与{@link不同。 #peek peek}只是因为如果这个队列为空它会抛出异常。
     *
     * <p>This implementation returns the result of <tt>peek</tt>
     * unless the queue is empty.
     *
     * @return the head of this queue
     * @throws NoSuchElementException if this queue is empty
     */
    @Test(expected = NoSuchElementException.class)
    public void testElement() {
        ArrayBlockingQueue<String> queue = example.create(2);
        queue.add("Hello1");
        queue.add("Hello2");
        ///////////////////////
        assertThat(queue.element(), equalTo("Hello1"));
        assertThat(queue.element(), equalTo("Hello1"));
        assertThat(queue.element(), equalTo("Hello1"));
        queue.clear();
        assertThat(queue.element(), equalTo("Hello1"));
    }


    /**
     * Retrieves and removes the head of this queue.  This method differs
     * from {@link. #poll poll} only in that it throws an exception if this
     * queue is empty.
     *
     * <p>This implementation returns the result of <tt>poll</tt>
     * unless the queue is empty.
     *
     * @return the head of this queue
     * @throws NoSuchElementException if this queue is empty
     */
    @Test(expected = NoSuchElementException.class)
    public void testRemove() {
        ArrayBlockingQueue<String> queue = example.create(2);
        queue.add("Hello1");
        queue.add("Hello2");
        ///////////////////////
        assertThat(queue.remove(), equalTo("Hello1"));
        assertThat(queue.remove(), equalTo("Hello1"));
        assertThat(queue.remove(), equalTo("Hello1"));
    }


    @Test
    public void testdrainTo() {
        ArrayBlockingQueue<String> queue = example.create(5);
        queue.add("Hello1");
        queue.add("Hello2");
        queue.add("Hello3");
        assertThat(queue.size(), equalTo(3));
        //剩余容量
        assertThat(queue.remainingCapacity(), equalTo(2));
        assertThat(queue.remove(), equalTo("Hello1"));
        assertThat(queue.remainingCapacity(), equalTo(3));
        assertThat(queue.size(), equalTo(2));

        List<String> list = new ArrayList<>();
        //排到某处
        queue.drainTo(list);

        assertThat(queue.remainingCapacity(), equalTo(5));
        assertThat(queue.size(), equalTo(0));

        assertThat(list.size(), equalTo(2));
    }

}
