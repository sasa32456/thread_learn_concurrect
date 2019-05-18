package com.n33.jcu.atomic;

import org.junit.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
/**
 * AtomicBoolean底层其实也是int value
 *
 * @author N33
 * @date 2019/5/19
 */
public class AtomicBooleanTest {

    @Test
    public void testCreateWithOutArguments() {
        AtomicBoolean bool = new AtomicBoolean();
        assertFalse(bool.get());
    }

    @Test
    public void testCreateWithArguments() {
        AtomicBoolean bool = new AtomicBoolean(true);
        assertTrue(bool.get());
    }

    @Test
    public void testGetAndSet() {
        AtomicBoolean bool = new AtomicBoolean(true);
        final boolean result = bool.getAndSet(false);
        assertTrue(result);
        assertFalse(bool.get());

    }

    /**
     * 期望更新
     */
    @Test
    public void testCompareAndSet() {
        AtomicBoolean bool = new AtomicBoolean(true);
        final boolean result = bool.compareAndSet(true, false);
        assertTrue(result);
        assertFalse(bool.get());

    }

    @Test
    public void testCompareAndSetFailed() {
        AtomicBoolean bool = new AtomicBoolean(true);
        final boolean result = bool.compareAndSet(false, true);
        assertFalse(result);
        assertTrue(bool.get());

    }




}
