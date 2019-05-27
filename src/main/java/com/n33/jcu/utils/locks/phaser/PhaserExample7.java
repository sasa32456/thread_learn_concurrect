package com.n33.jcu.utils.locks.phaser;

import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * 打断
 *
 * @author N33
 * @date 2019/5/27
 */
public class PhaserExample7 {
    public static void main(String[] args) throws InterruptedException {
//        final Phaser phaser = new Phaser(3);
//
//        final Thread thread = new Thread(phaser::arriveAndAwaitAdvance);
//        thread.start();
//        System.out.println("=================");
//        TimeUnit.SECONDS.sleep(1);
//
//        //并没有中断
//        thread.interrupt();
//        System.out.println("========================");


//        final Phaser phaser = new Phaser(3);
//        final Thread thread = new Thread(() -> {
//            try {
//                phaser.awaitAdvanceInterruptibly(phaser.getPhase());
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        });
//        thread.start();
//
//        System.out.println("=================");
//        TimeUnit.SECONDS.sleep(1);
//
//        //打断,只有打断或者getPhase不等时候
//        thread.interrupt();
//        System.out.println("========================");


        final Phaser phaser = new Phaser(3);
        final Thread thread = new Thread(() -> {
            try {
                phaser.awaitAdvanceInterruptibly(phaser.getPhase(),5,TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }
        });
        thread.start();


    }
}
