package com.n33.jcu.atmoic;

/**
 * java调用c
 * 不会C，哈哈，就这个意思，未来再补TT
 *
 * @author N33
 * @date 2019/5/21
 */
public class NativeTest {
    static {
        System.loadLibrary("hello");
    }

    private native void hi();

    /**
     * 参考UnSafe，估计
     * @param args
     */
    public static void main(String[] args) {
        //new Hello().hi();
    }
}
