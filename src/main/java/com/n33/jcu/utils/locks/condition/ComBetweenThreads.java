package com.n33.jcu.utils.locks.condition;

import java.util.concurrent.TimeUnit;

/**
 *
 *
 * @author N33
 * @date 2019/5/26
 */
public class ComBetweenThreads {

    private static  int data = 0;

    private static boolean noUse = true;

    private final static Object MONITOR = new Object();

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


    private static void sleep(long seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void buildData() {
        synchronized (MONITOR) {
            while (noUse) {
                try {
                    MONITOR.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            data++;
            sleep(1);
            System.out.println("P=>" + data);
            noUse = true;
            MONITOR.notifyAll();
        }
    }


    private static void useData() {
        synchronized (MONITOR) {
            while (!noUse) {
                try {
                    MONITOR.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            sleep(1);
            System.out.println("C=>" + data);
            noUse = false;
            MONITOR.notifyAll();
        }
    }

}
