package com.n33.jcu.utils.locks.condition;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Condition实现
 * notify , notifyAll
 * <p>
 * Condition依赖LOCK使用
 *
 * @author N33
 * @date 2019/5/26
 */
public class ConditionExample1 {

    private final static ReentrantLock lock = new ReentrantLock();

    private final static Condition condition = lock.newCondition();

    private static int data = 0;

    private static volatile boolean noUse = true;

    private static void buildData() {
        try {
            lock.lock();  //synchronized key word #monitor enter
            while (noUse) {
                condition.await(); //monitor.wait()
            }
            data++;
            Optional.of(Thread.currentThread().getName() + " P:" + data).ifPresent(System.out::println);
            TimeUnit.SECONDS.sleep(1);
            noUse = true;
            condition.signalAll(); // monitor.notify
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock(); //synchronized end #monitor end
        }
    }


    private static void useData() {
        try {
            lock.lock();
            while (!noUse) {
                condition.await();
            }
//            TimeUnit.SECONDS.sleep(1);
            Optional.of(Thread.currentThread().getName() + " C:" + data).ifPresent(System.out::println);
            noUse = false;
            condition.signalAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        System.out.println("========================================");
    }

    public static void main(String[] args) {

        new Thread(() -> {
            for (; ; ) {
                buildData();
            }
        }).start();

        for (int i = 0; i < 2; i++) {

            new Thread(() -> {
                for (; ; ) {
                    useData();
                }
            }).start();
        }


    }


}
