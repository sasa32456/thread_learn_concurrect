package com.n33.jcu.collections.blocking.synchronousqueue;

import org.junit.Test;

import java.util.concurrent.Executors;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

/**
 * 偷懒
 *
 * @author N33
 * @date 2019/6/10
 */
public class SynchronousQueueExampleTest {


    @Test
    public void testAdd() throws InterruptedException {
        final SynchronousQueue<Object> queue = SynchronousQueueExample.create();

        Executors.newSingleThreadExecutor().submit(() -> {
            try {
                assertThat(queue.take(), equalTo("SynchronousQueue"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        TimeUnit.MILLISECONDS.sleep(5);
        assertThat(queue.add("SynchronousQueue"), equalTo(true));
    }

    /**
     * 如果另一个线程正在等待接收它，则将指定的元素插入此队列。
     * <pre>
     * @param. e the element to add
     * @return {@code true} if the element was added to this queue, else
     *         {@code false}
     * @throws NullPointerException if the specified element is null
     *     public boolean offer(E e) {
     *         if (e == null) throw new NullPointerException();
     *         return transferer.transfer(e, true, 0) != null;
     *     }
     * </pre>
     * 有人要，则true
     * 反之，直接false
     * transferer类似定时完成，不完成直接放弃
     *
     * @throws InterruptedException
     */
    @Test
    public void testOffer() throws InterruptedException {
        final SynchronousQueue<Object> queue = SynchronousQueueExample.create();

        Executors.newSingleThreadExecutor().submit(() -> {
            try {
                assertThat(queue.take(), equalTo("SynchronousQueue"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        long startTime = System.currentTimeMillis();
        TimeUnit.MILLISECONDS.sleep(5);
        System.out.println(System.currentTimeMillis() - startTime);

        assertThat(queue.offer("SynchronousQueue"), equalTo(true));
    }


    @Test
    public void testPut() throws InterruptedException {
        final SynchronousQueue<Object> queue = SynchronousQueueExample.create();

//        Executors.newSingleThreadExecutor().submit(() -> {
//            try {
//                assertThat(queue.take(), equalTo("SynchronousQueue"));
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        });
//        TimeUnit.MILLISECONDS.sleep(5);

        queue.put("SynchronousQueue");
        fail("should not process to here.");
    }
}
