package com.n33.jcu.atomic;

import org.junit.Test;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

/**
 * 无法赋予安全线程
 *
 * @author N33
 * @date 2019/5/19
 */
public class FailedAtomicIntegerFieldUpdaterTest {

    /**
     * java.lang.RuntimeException: java.lang.IllegalAccessException: Class com.n33.jcu.atomic.FailedAtomicIntegerFieldUpdaterTest can not access a member of class com.n33.jcu.atomic.TestMe with modifiers "private volatile"
     * Caused by: java.lang.IllegalAccessException: Class com.n33.jcu.atomic.FailedAtomicIntegerFieldUpdaterTest can not access a member of class com.n33.jcu.atomic.TestMe with modifiers "private volatile"
     * 不能访问私有
     */
    @Test(expected = RuntimeException.class)
    public void testPrivateFieldAccessError() {
        AtomicIntegerFieldUpdater<TestMe> updater = AtomicIntegerFieldUpdater.newUpdater(TestMe.class, "i");
        TestMe me = new TestMe();
        updater.compareAndSet(me, 0, 1);
    }

    /**
     * java.lang.ClassCastException
     * 无法转换
     */
    @Test
    public void testTargetObjectIsNull() {
        AtomicIntegerFieldUpdater<TestMe> updater = AtomicIntegerFieldUpdater.newUpdater(TestMe.class, "i");
        updater.compareAndSet(null, 0, 1);
    }

    /**
     * java.lang.RuntimeException: java.lang.NoSuchFieldException: i1
     * Caused by: java.lang.NoSuchFieldException: i1
     * 反射获取不到对应名称
     */
    @Test
    public void testFieldNameInvalid() {
        AtomicIntegerFieldUpdater<TestMe> updater = AtomicIntegerFieldUpdater.newUpdater(TestMe.class, "i1");
        updater.compareAndSet(null, 0, 1);
    }


    /**
     * java.lang.ClassCastException
     * 类型不匹配
     * 当传入不是TestMe2，也会报错
     */
    @Test
    public void testFieldTypeInvalid() {
        AtomicReferenceFieldUpdater<TestMe2, Long> updater = AtomicReferenceFieldUpdater.newUpdater(TestMe2.class, Long.class, "i");
        TestMe2 me = new TestMe2();
        updater.compareAndSet(me, null, 1L);
    }

    /**
     * java.lang.IllegalArgumentException: Must be volatile type
     * 必须volatile
     */
    @Test
    public void testFieldIsNotVolatile() {
        AtomicReferenceFieldUpdater<TestMe2, Integer> updater = AtomicReferenceFieldUpdater.newUpdater(TestMe2.class, Integer.class, "i");
        TestMe2 me = new TestMe2();
        updater.compareAndSet(me, null, 1);
    }


    static class TestMe2 {
        //...
        //volatile Integer i;

        //protected也会报错

        //testFieldIsNotVolatile
        Integer i;
    }


}

