package com.n33.jcu.collections.blocking.priorityblockingqueue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Comparator;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

/**
 * like ArrayList
 *
 * @author N33
 * @date 2019/6/10
 */
public class PriorityBlockingQueueExampleTest {

    private PriorityBlockingQueueExample example;

    @Before
    public void setUp() {
        this.example = new PriorityBlockingQueueExample();
    }

    @After
    public void tearDown() {
        this.example = null;
    }


    /**
     * The {@link PriorityBlockingQueue#add(Object)}
     * {@link PriorityBlockingQueue#offer(Object)}
     * {@link PriorityBlockingQueue#put(Object)}
     * <p>
     * all of then are same
     */
    @Test
    public void testAddNewElement() {
        PriorityBlockingQueue<String> queue = example.create(2);

        assertThat(queue.add("Hello1"), equalTo(true));
        assertThat(queue.offer("Hello2"), equalTo(true));
        queue.put("Hello3");
        assertThat(queue.size(), equalTo(3));
    }


    /**
     * add 加
     * element 取，不拿出
     * peek 同上
     * poll 取，拿出
     * take 取，拿出，阻塞
     */
    @Test
    public void testGetElement() throws InterruptedException {
        PriorityBlockingQueue<String> queue = example.create(3);

        assertThat(queue.add("Hello4"), equalTo(true));
        assertThat(queue.add("Hello2"), equalTo(true));
        assertThat(queue.add("Hello3"), equalTo(true));

        assertThat(queue.element(), equalTo("Hello2"));
        assertThat(queue.size(), equalTo(3));
        assertThat(queue.element(), equalTo("Hello2"));
        //////////////////////////////////

        assertThat(queue.peek(), equalTo("Hello2"));
        assertThat(queue.size(), equalTo(3));
        assertThat(queue.peek(), equalTo("Hello2"));
        assertThat(queue.size(), equalTo(3));
        /////////////////////////

        assertThat(queue.poll(), equalTo("Hello2"));
        assertThat(queue.size(), equalTo(2));
        assertThat(queue.poll(), equalTo("Hello3"));
        assertThat(queue.size(), equalTo(1));
        ////////////////////
        //queue.remove();
        ////////////////

        /**
         * take()
         * blocked
         */
        assertThat(queue.take(), equalTo("Hello4"));
        assertThat(queue.size(), equalTo(0));

        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.schedule(() -> queue.add("NEW_DATA"), 1, TimeUnit.SECONDS);
        executorService.shutdown();
        assertThat(queue.take(), equalTo("NEW_DATA"));
    }


    @Test(expected = NullPointerException.class)
    public void testAddNullElement() throws InterruptedException {
        PriorityBlockingQueue<String> queue = example.create(3);
        queue.add(null);

    }

    /**
     * 不实现comparable则异常
     * _代表And
     */
    @Test(expected = ClassCastException.class)
    public void testAddObject_WithNoComparable_WithNoComparator() {
        PriorityBlockingQueue<UserWithNoComparable> queue = example.create(3);
        queue.add(new UserWithNoComparable());
        fail("should not process to here");
    }

    /**
     * 提供接口
     */
    @Test //(expected = ClassCastException.class)
    public void testAddObject_WithNoComparable_NoComparator() {
        PriorityBlockingQueue<UserWithNoComparable> queue = example.create(3, Comparator.comparingInt(Object::hashCode));
        queue.add(new UserWithNoComparable());
//        fail("should not process to here");
    }


    static class UserWithNoComparable {
    }

}

