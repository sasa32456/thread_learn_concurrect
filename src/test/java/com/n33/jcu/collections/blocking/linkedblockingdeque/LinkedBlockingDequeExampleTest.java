package com.n33.jcu.collections.blocking.linkedblockingdeque;

import org.junit.Test;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * This class is used for unit test {@link java.util.concurrent.LinkedBlockingDeque}
 * <p>
 * Double-End-Queue
 *
 * @author N33
 * @date 2019/6/12
 */
public class LinkedBlockingDequeExampleTest {

    /**
     * remove=removeFirst
     */
    @Test
    public void testAddFirst() {
        final LinkedBlockingDeque<String> deque = LinkedBlockingDequeExample.create();
        deque.addFirst("Java");
        deque.addFirst("Scala");
        assertThat(deque.removeFirst(), equalTo("Scala"));
        assertThat(deque.removeFirst(), equalTo("Java"));
    }


    @Test
    public void testAdd() {
        final LinkedBlockingDeque<String> deque = LinkedBlockingDequeExample.create();
        deque.add("Java");
        deque.add("Scala");
        assertThat(deque.remove(), equalTo("Java"));
        assertThat(deque.remove(), equalTo("Scala"));
    }


    @Test
    public void testTakeFirst() throws InterruptedException {
        LinkedBlockingDeque<String> deque = LinkedBlockingDequeExample.create();

        final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.schedule(() -> deque.add("TakeDeque"), 1, TimeUnit.SECONDS);
        executorService.shutdown();

        long currentTime = System.currentTimeMillis();
        deque.takeFirst();
        assertThat(System.currentTimeMillis() - currentTime >= 1000, equalTo(true));
    }

}
