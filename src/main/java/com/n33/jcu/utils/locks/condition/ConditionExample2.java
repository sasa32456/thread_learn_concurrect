package com.n33.jcu.utils.locks.condition;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 不用condition只用lock？不行，因为会抢锁，导致生产后还是生产
 * 不用lock只用condition？会释放掉cpu执行权，//Exception in thread "Thread-0" java.lang.IllegalMonitorStateException有lock才能用
 *
 * @author N33
 * @date 2019/5/26
 */
public class ConditionExample2 {


    private final static ReentrantLock lock = new ReentrantLock();

    private final static Condition condition = lock.newCondition();

    private static int data = 0;

    private static volatile boolean noUse = true;

    private static void buildData() {
        try {
//            lock.lock();  //synchronized key word #monitor enter
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
//            lock.unlock(); //synchronized end #monitor end
        }
    }


    private static void useData() {
        try {
//            lock.lock();
            while (!noUse) {
                condition.await();
            }
            TimeUnit.SECONDS.sleep(1);
            Optional.of(Thread.currentThread().getName() + " C:" + data).ifPresent(System.out::println);
            noUse = false;
            condition.signalAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
//            lock.unlock();
        }
    }

    public static void main(String[] args) {

        new Thread(() -> {
            for (; ; ) {
                buildData();
            }
        }).start();


            new Thread(() -> {
                for (; ; ) {
                    useData();
                }
            }).start();


    }

}
