package com.n33.jcu.collections.blocking.delayqueue;

import org.junit.Test;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.*;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
/**
 * add = put = offer
 *
 * @author N33
 * @date 2019/6/12
 */
public class DelayQueueExampleTest {

    /**
     * Add method must add the Delayed element.
     * peek method will return null/element(but not remove) be quickly.
     *
     * @throws InterruptedException
     */
    @Test
    public void testAdd1() throws InterruptedException {
        DelayQueue<DelayElement<String>> delayQueue = DelayQueueExample.create();
        final DelayElement<String> delayed1 = DelayElement.of("Delayed1", 1000);
        delayQueue.add(delayed1);
        assertThat(delayQueue.size(), equalTo(1));
        long startTime = System.currentTimeMillis();
        assertThat(delayQueue.peek(), is(delayed1));
        assertThat(delayQueue.peek(), is(delayed1));
//        final DelayElement<String> take = delayQueue.take();
//        System.out.println(take.getData());
        System.out.println(System.currentTimeMillis() - startTime);
    }


    /**
     * 迭代器无延迟
     * @throws InterruptedException
     */
    @Test
    public void testAdd2() throws InterruptedException {
        DelayQueue<DelayElement<String>> delayQueue = DelayQueueExample.create();
        delayQueue.add(DelayElement.of("Delayed1", 1000));
        delayQueue.add(DelayElement.of("Delayed1", 800));
        delayQueue.add(DelayElement.of("Delayed1", 11000));
        delayQueue.add(DelayElement.of("Delayed1", 20000));
        delayQueue.add(DelayElement.of("Delayed1", 3000));

        assertThat(delayQueue.size(), equalTo(5));
        long startTime = System.currentTimeMillis();

        Iterator<DelayElement<String>> it = delayQueue.iterator();
        while (it.hasNext()) {
            assertThat(it.next(), notNullValue());
        }

        assertThat(System.currentTimeMillis() - startTime < 5, equalTo(true));
        System.out.println(System.currentTimeMillis() - startTime);
    }



    @Test
    public void testAdd3() throws InterruptedException {
        DelayQueue<DelayElement<String>> delayQueue = DelayQueueExample.create();

        delayQueue.add(DelayElement.of("Delayed1", 1000));
        delayQueue.add(DelayElement.of("Delayed2", 800));
        delayQueue.add(DelayElement.of("Delayed3", 11000));
        delayQueue.add(DelayElement.of("Delayed4", 20000));
        delayQueue.add(DelayElement.of("Delayed5", 3000));

        assertThat(delayQueue.size(), equalTo(5));
        assertThat(delayQueue.take().getData(),equalTo("Delayed2") );
        assertThat(delayQueue.take().getData(),equalTo("Delayed1") );
        assertThat(delayQueue.take().getData(),equalTo("Delayed5") );
        assertThat(delayQueue.take().getData(),equalTo("Delayed3") );
        assertThat(delayQueue.take().getData(),equalTo("Delayed4") );
    }





    /**
     * 立即返回过期元素，没过期，则返回null
     * @throws InterruptedException
     */
    @Test(expected = NoSuchElementException.class)
    public void testAdd4() throws InterruptedException {
        DelayQueue<DelayElement<String>> delayQueue = DelayQueueExample.create();

        delayQueue.add(DelayElement.of("Delayed1", 1000));
        delayQueue.add(DelayElement.of("Delayed2", 800));
        delayQueue.add(DelayElement.of("Delayed3", 11000));
        delayQueue.add(DelayElement.of("Delayed4", 20000));
        delayQueue.add(DelayElement.of("Delayed5", 3000));

        assertThat(delayQueue.size(), equalTo(5));
        long startTime = System.currentTimeMillis();
        Iterator<DelayElement<String>> it = delayQueue.iterator();
        assertThat(delayQueue.remove().getData(), equalTo("Delayed2"));
        while (it.hasNext()) {
            assertThat(it.next(), notNullValue());
        }

        assertThat(System.currentTimeMillis() - startTime < 5, equalTo(true));
        System.out.println(System.currentTimeMillis() - startTime);
    }


    /**
     * 已经过期会立即返回
     * @throws InterruptedException
     */
    @Test
    public void testAdd5() throws InterruptedException {
        DelayQueue<DelayElement<String>> delayQueue = DelayQueueExample.create();

        delayQueue.add(DelayElement.of("Delayed1", 1000));
        delayQueue.add(DelayElement.of("Delayed2", 800));
        delayQueue.add(DelayElement.of("Delayed3", 11000));
        delayQueue.add(DelayElement.of("Delayed4", 20000));
        delayQueue.add(DelayElement.of("Delayed5", 3000));

        TimeUnit.MILLISECONDS.sleep(810);
        long startTime = System.currentTimeMillis();
        assertThat(delayQueue.size(), equalTo(5));
//        assertThat(delayQueue.poll().getData(), equalTo("Delayed2"));
        assertThat(delayQueue.take().getData(), equalTo("Delayed2"));
        System.out.println(System.currentTimeMillis() - startTime);
    }


    @Test
    public void testAdd6() throws InterruptedException {
        DelayQueue<DelayElement<String>> delayQueue = DelayQueueExample.create();
        try {
            delayQueue.add(null);
            fail("should not process to here");
        } catch (Exception e) {
            assertThat(e instanceof NullPointerException,equalTo(true));
        }
    }

    /**
     * poll立即取，取不到为null
     */
    @Test
    public void testPoll() throws InterruptedException {
        DelayQueue<DelayElement<String>> delayQueue = DelayQueueExample.create();
        delayQueue.add(DelayElement.of("Delayed1", 1000));
        assertThat(delayQueue.poll(), nullValue());
    }

    @Test
    public void testTake() throws InterruptedException {
        DelayQueue<DelayElement<String>> delayQueue = DelayQueueExample.create();
        final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.schedule(() -> delayQueue.add(DelayElement.of("Test", 1000)), 1, TimeUnit.SECONDS);
        executorService.shutdown();
        long startTime = System.currentTimeMillis();
        assertThat(delayQueue.take().getData(), equalTo("Test"));
        assertThat(System.currentTimeMillis() - startTime >= 1000, equalTo(true));
        System.out.println(System.currentTimeMillis() - startTime);
    }

    static class DelayElement<E> implements Delayed{

        private E e;

        private final long expireTime;

        public DelayElement(E e, long expireTime) {
            this.e = e;
            this.expireTime = System.currentTimeMillis() + expireTime;
        }

        static <E> DelayElement<E> of(E e, long delay) {
            return new DelayElement<>(e, delay);
        }

        @Override
        public long getDelay(TimeUnit unit) {
            long diff = expireTime - System.currentTimeMillis();
            return unit.convert(diff, TimeUnit.MILLISECONDS);
        }

        @Override
        public int compareTo(Delayed delayObject) {
            DelayElement that = (DelayElement) delayObject;
            if (this.expireTime < that.getExpireTime()) {
                return -1;
            } else if (this.expireTime > that.getExpireTime()) {
                return 1;
            } else {
                return 0;
            }
        }

        public E getData() {
            return e;
        }

        public long getExpireTime() {
            return expireTime;
        }
    }

}
