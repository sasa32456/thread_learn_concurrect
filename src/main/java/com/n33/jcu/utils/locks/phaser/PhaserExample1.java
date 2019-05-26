package com.n33.jcu.utils.locks.phaser;

import java.util.Random;
import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * 汲取CountDownLatch计数器以及
 * CyclicBarrier的复位优点并改进
 * 增加动态
 *
 * @author N33
 * @date 2019/5/26
 */
public class PhaserExample1 {

    private final static Random random = new Random(System.currentTimeMillis());

    public static void main(String[] args) {

        final Phaser phaser = new Phaser();

        IntStream.rangeClosed(1, 5).boxed().map(i -> phaser).forEach(Task::new);

        phaser.register();

        phaser.arriveAndAwaitAdvance();
        System.out.println("All of worker finished the task");
    }

    static class Task extends Thread {
        private final Phaser phaser;

        public Task(Phaser phaser) {
            this.phaser = phaser;
            phaser.register();
            start();
        }

        @Override
        public void run() {
            System.out.println("The Worker [" + getName() + "] is working...");
            try {
                TimeUnit.SECONDS.sleep(random.nextInt(5));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //到达并等待前行
            phaser.arriveAndAwaitAdvance();
        }
    }





}
