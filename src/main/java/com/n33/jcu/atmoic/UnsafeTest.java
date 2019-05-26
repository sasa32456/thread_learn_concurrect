package com.n33.jcu.atmoic;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
/**
 * Unsafe初学
 * Unsafe调用汇编c++，cpu级别
 * @author N33
 * @date 2019/5/21
 */
public class UnsafeTest {
    public static void main(String[] args) throws Exception {
        /**
         * Exception in thread "main" java.lang.SecurityException: Unsafe
         * 	at sun.misc.Unsafe.getUnsafe(Unsafe.java:90)
         * 	at com.n33.jcu.atmoic.UnsafeTest.main(UnsafeTest.java:7)
         */
//        Unsafe unsafe = Unsafe.getUnsafe();
//        System.out.println(unsafe);
//        Unsafe unsafe = getUnsafe();
//        System.out.println(unsafe);


        /**
         * StupidCounter
         * Counter result: 9931220
         * Time passed in ms : 245
         *
         * SyncCounter
         * Counter result: 10000000
         * Time passed in ms : 643
         *
         * LockCounter
         * Counter result: 10000000
         * Time passed in ms : 315
         *
         *
         * AtomicCounter
         * Counter result: 10000000
         * Time passed in ms : 338
         *
         * CasCounter 理论比Atomic快，不知道是不是打错了
         * Counter result: 10000000
         * Time passed in ms : 1425
         */
        ExecutorService service = Executors.newFixedThreadPool(1000);
        //Counter counter = new StupidCounter();
        //Counter counter = new SyncCounter();
        //Counter counter = new LockCounter();
        //Counter counter = new AtomicCounter();
        Counter counter = new CasCounter();
        long start = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            service.submit(new CounterRunnable(counter, 10000));
        }

        service.shutdown();
        service.awaitTermination(1, TimeUnit.HOURS);

        long end = System.currentTimeMillis();

        System.out.println("Counter result: " + counter.getCounter());
        System.out.println("Time passed in ms : " + (end - start));





    }
    private static Unsafe getUnsafe() {
        try {
            Field f = Unsafe.class.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            return (Unsafe) f.get(null);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }


    interface Counter {
        void increment();
        long getCounter();
    }

    static class StupidCounter implements Counter {
        private long counter = 0;
        @Override
        public void increment() {
            counter++;
        }
        @Override
        public long getCounter() {
            return counter;
        }
    }


    static class SyncCounter implements Counter {
        private long counter = 0;
        @Override
        public synchronized void increment() {
            counter++;
        }
        @Override
        public long getCounter() {
            return counter;
        }
    }


    static class LockCounter implements Counter {
        private long counter = 0;
        /**
         *
         * Creates an instance of {@code reentrantlock}.
         * This is equivalent to using {@code reentrantlock(false)}.
         * 默认不公平
         */
        private final Lock lock = new ReentrantLock();
        @Override
        public  void increment() {
            try {
                lock.lock();
                counter++;
            } finally {
                lock.unlock();
            }
        }
        @Override
        public long getCounter() {
            return counter;
        }
    }


    static class AtomicCounter implements Counter {
        private AtomicLong counter = new AtomicLong(0);
        @Override
        public void increment() {
            counter.incrementAndGet();
        }
        @Override
        public long getCounter() {
            return counter.get();
        }
    }


    static class CasCounter implements Counter {
        private volatile long counter = 0;
        private Unsafe unsafe;
        private long offset;
        CasCounter() throws Exception {
            unsafe = getUnsafe();
            offset = unsafe.objectFieldOffset(CasCounter.class.getDeclaredField("counter"));
        }
        @Override
        public void increment() {
            long currect = counter;
            //不成功才去做
            while (!unsafe.compareAndSwapLong(this, offset, currect, currect + 1)) {
                currect = counter;
            }
        }
        @Override
        public long getCounter() {
            return counter;
        }
    }


    static class CounterRunnable implements Runnable {

        private final Counter counter;
        private final int num;

        public CounterRunnable(Counter counter, int num) {
            this.counter = counter;
            this.num = num;
        }

        @Override
        public void run() {
            for (int i = 0; i < num; i++) {
                counter.increment();
            }
        }
    }
}


