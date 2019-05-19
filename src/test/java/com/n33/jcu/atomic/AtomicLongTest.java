package com.n33.jcu.atomic;

import org.junit.Test;

import java.util.concurrent.atomic.AtomicLong;

import static org.junit.Assert.assertEquals;

public class AtomicLongTest {

    @Test
    public void testCreate() {
        AtomicLong l = new AtomicLong(100L);

        /**
         * 32
         * long 64
         *
         * high 32
         * low 32
         *
         */
        assertEquals(100L,l.get());
    }


    /**
     *
     * 部分不支持64位的要分两次拿32位int拼成long，这时候需要判断是否混乱 VM_SUPPORTS_LONG_CAS
     *
     * Records whether the underlying JVM supports lockless
     * compareAndSwap for longs. While the Unsafe.compareAndSwapLong
     * method works in either case, some constructions should be
     * handled at Java level to avoid locking user-visible locks.
     * 记录底层JVM是否支持longs的无锁compareAndSwap。
     * 虽然Unsafe.compareAndSwapLong方法适用于任何一种情况，
     * 但是应该在Java级别处理一些构造以避免锁定用户可见的锁。
     * static final boolean VM_SUPPORTS_LONG_CAS = VMSupportsCS8();
     */

}
