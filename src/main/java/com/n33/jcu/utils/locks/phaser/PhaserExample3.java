package com.n33.jcu.utils.locks.phaser;

import java.util.Random;
import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;
/**
 * phaser.arriveAndDeregister();
 * 动态注销
 *
 * @author N33
 * @date 2019/5/27
 */
public class PhaserExample3 {

    private final static Random random = new Random(System.currentTimeMillis());

    public static void main(String[] args) {

        final Phaser phaser = new Phaser(5);

        for (int i = 1; i <= 4; i++) {
            new Athletes(i, phaser).start();
        }
        new InjureAthletes(5,phaser).start();

    }

    static class InjureAthletes extends Thread {

        private final int no;

        private final Phaser phaser;

        public InjureAthletes(int no, Phaser phaser) {
            this.no = no;
            this.phaser = phaser;
        }


        @Override
        public void run() {
            try {
                sport(no, phaser, ": start running.", ": end running.");

                sport(no, phaser, ": start bicycle.", ": end bicycle.");

//                System.out.println("Oh shit  ,i am injured");
                //---------------------------------------------
                System.out.println("Oh shit  ,i am injured, i will be exited.");
                //注销一个
                phaser.arriveAndDeregister();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    static class Athletes extends Thread {

        private final int no;

        private final Phaser phaser;

        public Athletes(int no, Phaser phaser) {
            this.no = no;
            this.phaser = phaser;
        }


        @Override
        public void run() {
            try {
                sport(no, phaser, ": start running.", ": end running.");

                sport(no, phaser, ": start bicycle.", ": end bicycle.");

                sport(no, phaser, ": start long jump.", ": end long jump.");


            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
    private static void sport(int no, Phaser phaser, String s, String s2) throws InterruptedException {
        System.out.println(no + s);
        TimeUnit.SECONDS.sleep(random.nextInt(5));
        System.out.println(no + s2);

        phaser.arriveAndAwaitAdvance();
    }

}
