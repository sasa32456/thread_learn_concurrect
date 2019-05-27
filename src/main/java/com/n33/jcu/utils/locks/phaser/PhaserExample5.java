package com.n33.jcu.utils.locks.phaser;

import java.util.Random;
import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;

/**
 * arrive
 *
 * @author N33
 * @date 2019/5/27
 */
public class PhaserExample5 {

    private final static Random random = new Random(System.currentTimeMillis());

    public static void main(String[] args) throws InterruptedException {

//        final Phaser phaser = new Phaser(3);
//
//        //到达不等待,不能在0的时候使用
//        //Exception in thread "Thread-0" java.lang.IllegalStateException: Attempted arrival of unregistered party for java.util.concurrent.Phaser@7be2d776[phase = 0 parties = 0 arrived = 0]
//        new Thread(phaser::arrive).start();
//
//        TimeUnit.SECONDS.sleep(2);


        final Phaser phaser = new Phaser(5);

        for (int i = 0; i < 4; i++) {
            new ArriveTask(phaser, i).start();
        }

        phaser.arriveAndAwaitAdvance();

        System.out.println("The phase 1 work finished done.");



    }


    private static class ArriveTask extends Thread {
        private final Phaser phaser;

        public ArriveTask(Phaser phaser, int no) {
            super(String.valueOf(no));
            this.phaser = phaser;
        }

        @Override
        public void run() {
            System.out.println(getName() + " start working.");
            PhaserExample5.sleep();
            System.out.println(getName() + " the phase one is running ");
            phaser.arrive();

            PhaserExample5.sleep();
            System.out.println(getName() + " keep to do other thing.");


        }
    }

    private static void sleep() {
        try {
            TimeUnit.SECONDS.sleep(random.nextInt(5));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


}
