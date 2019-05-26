package com.n33.jcu.utils.locks.stampedlock;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.StampedLock;
/**
 * 不知道为啥会只写
 * 不管了
 * @author N33
 * @date 2019/5/26
 */
public class MyTest {

    private final static StampedLock lock = new StampedLock();

    private final static StringBuilder DATA = new StringBuilder();

    public static void main(String[] args) {


        final ExecutorService executorService = Executors.newFixedThreadPool(10);

        for (int i = 0; i < 9; i++) {
            executorService.submit(() -> {
                while (true) {
                    read();
                }
            });
        }

        executorService.submit(() -> {
            while (true) {
                write();
            }
        });

    }

    private static void read() {
        long stamp = lock.tryOptimisticRead();
        if (lock.validate(stamp)) {
            try {
                stamp = lock.readLock();
                System.out.println(Thread.currentThread().getName() + " read: " + DATA);
//                TimeUnit.SECONDS.sleep(1);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
            } finally {
                lock.unlockRead(stamp);
            }

        }
    }

    private static void write() {
        long stamp = lock.writeLock();
        try {
            DATA.append(System.currentTimeMillis());
            System.out.println(Thread.currentThread().getName()+ " is writing...");
//            TimeUnit.SECONDS.sleep(1);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
        } finally {
            lock.unlockWrite(stamp);
        }

    }
}
