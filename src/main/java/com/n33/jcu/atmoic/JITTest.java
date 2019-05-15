package com.n33.jcu.atmoic;
/**
 * jdk8 不会停
 * jdk6 会停
 * 每代JDK对JIT不同
 *
 * @author N33
 * @date 2019/5/15
 */
public class JITTest {

    private static boolean init = false;

    public static void main(String[] args) throws InterruptedException {

        new Thread() {
            @Override
            public void run() {
                while (!init) {
                    //加上这句话就会退出
                    System.out.println(".");
                }
                /**
                 * JIT做的处理
                 * !Boolean的处理，当没有内容等价于
                 * while(true){}
                 * 当有，那么不改变
                 */
            }
        }.start();

        Thread.sleep(1000);

        new Thread() {
            @Override
            public void run() {
                init = true;
                System.out.println("Set init to true.");
            }
        }.start();
    }
}
