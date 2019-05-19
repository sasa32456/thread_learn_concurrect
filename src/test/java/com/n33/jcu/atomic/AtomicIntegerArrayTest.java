package com.n33.jcu.atomic;

import org.junit.Test;

import java.util.concurrent.atomic.AtomicIntegerArray;

import static org.junit.Assert.assertEquals;

/**
 * 数组
 *
 * @author N33
 * @date 2019/5/19
 */
public class AtomicIntegerArrayTest {


    //CAS数组
    //AtomicReferenceArray

    @Test
    public void testCreateAtomicIntegerArray() {
        /**
         * Creates a new AtomicIntegerArray of the given length, with all
         * elements initially zero.
         * 创建一个给定长度的新AtomicIntegerArray，所有元素最初为零。
         *
         * 最终现场保证保证可见性
         *
         * @param length the length of the array
         */
        AtomicIntegerArray array = new AtomicIntegerArray(10);
        assertEquals(10, array.length());
    }


    @Test
    public void testGet() {
        AtomicIntegerArray array = new AtomicIntegerArray(10);
        array.set(5, 5);
        assertEquals(10, array.length());
        assertEquals(5, array.get(5));
    }

    @Test
    public void testGetAndSet() {
        final int[] originalArray = new int[10];
        originalArray[5] = 5;
        AtomicIntegerArray array = new AtomicIntegerArray(originalArray);
        final int v = array.getAndSet(5, 6);
        assertEquals(5, v);
        assertEquals(6, array.get(5));
    }
}
