package com.n33.jcu.utils.locks.phaser;

import java.util.Random;
import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;

/**
 * 铁人三项
 * 到达后重新计算
 *
 * @author N33
 * @date 2019/5/26
 */
public class PhaserExample2 {
    /**
     * running
     * <p>
     * bicycle
     * <p>
     * long jump
     */
    private final static Random random = new Random(System.currentTimeMillis());

    public static void main(String[] args) {

        final Phaser phaser = new Phaser(5);

        for (int i = 0; i < 5; i++) {
                new Athletes(i, phaser).start();
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
                System.out.println(no + ": start running.");
                TimeUnit.SECONDS.sleep(random.nextInt(5));
                System.out.println(no + ": end running.");

                phaser.arriveAndAwaitAdvance();

//                System.out.println("getPhase()=>" + phaser.getPhase());

                System.out.println(no + ": start bicycle.");
                TimeUnit.SECONDS.sleep(random.nextInt(5));
                System.out.println(no + ": end bicycle.");

                phaser.arriveAndAwaitAdvance();

//                System.out.println("getPhase()=>" + phaser.getPhase());

                System.out.println(no + ": start long jump.");
                TimeUnit.SECONDS.sleep(random.nextInt(5));
                System.out.println(no + ": end long jump.");

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


}
