package com.n33.jcu.collections.blocking.linkedblockingqueue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.LinkedBlockingQueue;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;

/**
 * This class is used for unit test the class {@link LinkedBlockingQueueExample}
 *
 * @see {@link LinkedBlockingQueue}
 *
 * @author N33
 * @date 2019/6/10
 */
public class LinkedBlockingQueueExampleTest {

    private LinkedBlockingQueueExample example;

    @Before
    public void setUp() {
        this.example = new LinkedBlockingQueueExample();
    }

    @After
    public void tearDown() {
        this.example = null;
    }

    @Test
    public void testInsertData() throws InterruptedException {
        LinkedBlockingQueue<String> queue = example.create(2);
        assertThat(queue.offer("data1"), equalTo(true));
        assertThat(queue.offer("data2"), equalTo(true));
        assertThat(queue.offer("data3"), equalTo(false));

        queue.clear();

        assertThat(queue.add("data1"), equalTo(true));
        assertThat(queue.add("data2"), equalTo(true));

        /**
         * blocked
         */
        queue.put("data3");
    }


    @Test
    public void testRemoveData() throws InterruptedException {
        LinkedBlockingQueue<String> queue = example.create(2);
        assertThat(queue.offer("data1"), equalTo(true));
        assertThat(queue.offer("data2"), equalTo(true));
        assertThat(queue.offer("data3"), equalTo(false));

        /**
         * 拿不出来异常
         */
        assertThat(queue.element(), equalTo("data1"));
        assertThat(queue.element(), equalTo("data1"));


        assertThat(queue.peek(), equalTo("data1"));
        assertThat(queue.peek(), equalTo("data1"));


        assertThat(queue.remove(), equalTo("data1"));
        assertThat(queue.poll(), equalTo("data2"));
        assertThat(queue.size(), equalTo(0));
        assertThat(queue.remainingCapacity(), equalTo(2));


        assertThat(queue.take(), equalTo("xxx"));
        fail("should not process to here");
    }

}
