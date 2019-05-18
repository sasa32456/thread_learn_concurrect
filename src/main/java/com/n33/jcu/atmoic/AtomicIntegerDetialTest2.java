package com.n33.jcu.atmoic;

public class AtomicIntegerDetialTest2 {

    private final static CompareAndSetLock lock = new CompareAndSetLock();

    public static void main(String[] args) {
        for (int i = 0; i < 2; i++) {
            new Thread(() -> {
                try {
                    doSomeThing2();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (GetLockException e) {
                    e.printStackTrace();
                }
            }).start();
        }


    }

    private static void doSomeThing() throws InterruptedException {
        synchronized (AtomicIntegerDetialTest2.class) {
            System.out.println(Thread.currentThread().getName() + " get the lock");
            Thread.sleep(1000000);

        }
    }


    private static void doSomeThing2() throws InterruptedException, GetLockException {
        try {
            lock.tryLock();

            System.out.println(Thread.currentThread().getName() + " get the lock");
            Thread.sleep(1000000);

        } finally {
            lock.unLock();
        }
    }
}
