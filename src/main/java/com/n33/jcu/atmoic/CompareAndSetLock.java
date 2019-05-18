package com.n33.jcu.atmoic;

import java.util.concurrent.atomic.AtomicInteger;
/**
 * 立即失败锁，当抢不到锁就放弃
 *
 * @author N33
 * @date 2019/5/19
 */
public class CompareAndSetLock {
    private final AtomicInteger value = new AtomicInteger(0);
    /**
     * 必须判定，不然finally会释放锁
     */
    private Thread lockedThread;

    public void tryLock() throws GetLockException {
        final boolean success = value.compareAndSet(0, 1);
        if (!success) {
            throw new GetLockException("Get the lock failed");
        } else {
            lockedThread = Thread.currentThread();
        }

    }

    public void unLock() {
        if (0 == value.get()) {
            return;
        }
        if (lockedThread == Thread.currentThread()) {
            value.compareAndSet(1, 0);
        }
    }
}
