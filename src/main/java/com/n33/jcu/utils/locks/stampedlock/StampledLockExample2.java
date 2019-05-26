package com.n33.jcu.utils.locks.stampedlock;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.StampedLock;
import java.util.stream.Collectors;

/**
 * 乐观形式
 *
 * @author N33
 * @date 2019/5/26
 */
public class StampledLockExample2 {

    private final static StampedLock lock = new StampedLock();
    private final static List<Long> DATA = new ArrayList<>();

    public static void main(String[] args) {

        final ExecutorService executor = Executors.newFixedThreadPool(10);

        Runnable readTask = () -> {
            for (; ; ) {
                read();
            }
        };

        Runnable writeTask = () -> {
            for (; ; ) {
                write();
            }
        };


        for (int i = 0; i < 9; i++) {
            executor.submit(readTask);
        }
        /**
         * 有问题，一直写。。。奇葩,去掉休眠就可以了不过。。。
         */
        executor.submit(writeTask);

//
//        for (int i = 0; i < 9; i++) {
//            new Thread(readTask).start();
//        }
//        new Thread(writeTask).start();

        System.out.println("start");

    }

    private static void read() {

        long stamp = lock.tryOptimisticRead();
        if (lock.validate(stamp)) {
            try {
                stamp = lock.readLock();
//                System.err.println(stamp);
                Optional.of(Thread.currentThread().getName()+ " read : " +DATA.stream().map(String::valueOf).collect(Collectors.joining("#", "R-", ""))).ifPresent(System.out::println);
//                TimeUnit.SECONDS.sleep(1);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
            } finally {
                lock.unlockRead(stamp);
            }
        }

    }

    private static void write() {
        long stamp = -1;
        try {
            //悲观
            stamp = lock.writeLock();
//            System.err.println(stamp);
            DATA.add(System.currentTimeMillis());
            System.out.println(Thread.currentThread().getName()+" is writing...");
//            TimeUnit.SECONDS.sleep(1);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
        } finally {
            lock.unlockWrite(stamp);
        }

    }

}
