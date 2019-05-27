package com.n33.jcu.utils.locks.phaser;

import java.sql.Time;
import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * awaitAdvance
 *
 * @author N33
 * @date 2019/5/27
 */
public class PhaserExample6 {
    /**
     * awaitAdvane can decremental the arrived parties?
     * @param args
     */
    public static void main(String[] args) throws InterruptedException {
//        final Phaser phaser = new Phaser(6);
        //不参与计算,一样会block
//        new Thread(() -> phaser.awaitAdvance(phaser.getPhase())).start();
////        new Thread(() -> phaser.arrive()).start();
//
//        TimeUnit.SECONDS.sleep(3);
//        System.out.println(phaser.getArrivedParties());

//        phaser.awaitAdvance(phaser.getPhase());
//        System.out.println("=============");

        final Phaser phaser = new Phaser(6);

        IntStream.rangeClosed(0, 5).boxed().map(i -> phaser).forEach(AwaitAdvanceTask::new);
        TimeUnit.SECONDS.sleep(2);
        System.out.println(phaser.getPhase());
        phaser.awaitAdvance(phaser.getPhase());
        System.out.println("====================");
    }


    private static class AwaitAdvanceTask extends Thread {
        private final Phaser phaser;

        public AwaitAdvanceTask(Phaser phaser) {
            this.phaser = phaser;
            start();
        }

        @Override
        public void run() {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            phaser.arriveAndAwaitAdvance();
            System.out.println(getName()+" finished work.");
        }
    }



}
    /**
     * 等待该相位器的相位从给定相位值前进，如果当前相位不等于给定相位值或该相位器终止则立即返回。
     *
     * @param phase an arrival phase number, or negative value if
     * terminated; this argument is normally the value returned by a
     * previous call to {@code arrive} or {@code arriveAndDeregister}.
     * @return the next arrival phase number, or the argument if it is
     * negative, or the (negative) {@linkplain #getPhase() current phase}
     * if terminated
     *     public int awaitAdvance(int phase) {
     */
