package com.n33.jcu.collections.blocking.linkedtransferqueue;

import org.junit.Test;

import java.util.List;
import java.util.concurrent.*;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class LinkedTransferQueueExampleTest {


    /**
     * 如果可能，立即将元素转移给等待的消费者。
     *
     * <p>更准确地说，如果存在已经等待接收它的消费者（在{@link。#take}或定时{@link。＃pix（long，TimeUnit）poll}），
     * 则立即传送指定的元素，否则返回{ @code false}不添加元素。
     * 此方法等同于SynchronousQueue
     * <p>
     * Question:
     * When return the false that means at this time no consumer waiting, how about the element?
     * will store into the queue
     * <p></p>
     * Answer:
     * Without enqueuing the element.
     *
     * @throws NullPointerException if the specified element is null
     */
    @Test
    public void testTryTransfer() {
        final LinkedTransferQueue<String> queue = LinkedTransferQueueExample.create();
        final boolean result = queue.tryTransfer("Transfer");
        assertThat(result, equalTo(false));
        assertThat(queue.size(), equalTo(0));
    }

    /**
     * 将元素传输给消费者，必要时等待。
     *
     * <p>更确切地说，如果存在已经等待接收它的消费者（在{@link. #take}或定时{@link. #poll（long，TimeUnit）poll}），
     * 则立即传送指定的元素，否则插入指定的元素在此队列的尾部，等待消费者收到该元素。
     *
     * @throws NullPointerException if the specified element is null
     */
    @Test
    public void testTransfer() throws InterruptedException {
        final LinkedTransferQueue<String> queue = LinkedTransferQueueExample.create();

        final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.schedule(() -> {
            try {
                queue.take();
                assertThat(queue.take(), equalTo("Transfer"));
            } catch (InterruptedException e) {
                fail();
            }
        }, 1, TimeUnit.SECONDS);
        executorService.shutdown();
        /**
         * blocked
         */
        long currentTime = System.currentTimeMillis();
        queue.transfer("Transfer");
        assertThat(System.currentTimeMillis() - currentTime >= 1000, equalTo(true));
        assertThat(queue.size(), equalTo(0));

    }


    @Test
    public void testTransfer2() throws InterruptedException {
        final LinkedTransferQueue<String> queue = LinkedTransferQueueExample.create();
        assertThat(queue.getWaitingConsumerCount(), equalTo(0));
        assertThat(queue.hasWaitingConsumer(), equalTo(false));

        final List<Callable<String>> collect = IntStream.range(0, 5).boxed().map(i -> (Callable<String>) () -> {
            try {
                return queue.take();
            } catch (InterruptedException e) {
                throw new RuntimeException();
            }
        }).collect(toList());

        final ExecutorService executorService = Executors.newCachedThreadPool();
        collect.stream().forEach(executorService::submit);


        TimeUnit.MILLISECONDS.sleep(5);
        assertThat(queue.getWaitingConsumerCount(), equalTo(5));
        assertThat(queue.hasWaitingConsumer(), equalTo(true));

        IntStream.range(0, 5).boxed().map(String::valueOf).forEach(queue::add);
        TimeUnit.MILLISECONDS.sleep(5);

        assertThat(queue.getWaitingConsumerCount(), equalTo(0));
        assertThat(queue.hasWaitingConsumer(), equalTo(false));

    }


    /**
     * 将指定元素插入此队列的尾部。由于队列是无限制的，因此该方法永远不会抛出{@link IllegalStateException}或返回{@code false}。
     *
     * @return {@code true} (as specified by {@link. Collection#add})
     * @throws NullPointerException if the specified element is null
     */
    @Test
    public void testAdd() {
        final LinkedTransferQueue<String> queue = LinkedTransferQueueExample.create();
        assertThat(queue.add("Hello"), equalTo(true));
        assertThat(queue.size(), equalTo(true));
    }

}
