package com.n33.jcu.atmoic;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * 解决CAS
 * 乐观锁
 *
 * @author N33
 * @date 2019/5/19
 */
public class AtomicStampedReferenceTest {

    /**
     * Creates a new {@code AtomicStampedReference} with the given
     * initial values.
     *
     * @param initialRef the initial reference
     * @param initialStamp the initial stamp  时间戳，乐观锁version一样
     */
    private static AtomicStampedReference<Integer> atomicRef = new AtomicStampedReference<>(100, 0);

    public static void main(String[] args) throws InterruptedException {

        Thread t1 = new Thread(()->{
            try {
                TimeUnit.SECONDS.sleep(1);
                boolean success = atomicRef.compareAndSet(100, 101, atomicRef.getStamp(), atomicRef.getStamp() + 1);
                System.out.println(success);
                success = atomicRef.compareAndSet(101, 100, atomicRef.getStamp(), atomicRef.getStamp() + 1);
                System.out.println(success);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread t2 = new Thread(()->{
            try {
                final int stamp = atomicRef.getStamp();
                System.out.println("Before sleep:stamp=" + stamp);
                //睡两秒
                TimeUnit.SECONDS.sleep(2);


                final boolean success = atomicRef.compareAndSet(100, 101, stamp, stamp + 1);
                System.out.println(success);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });


        t1.start();
        t2.start();
        t1.join();
        t2.join();

    }
}
