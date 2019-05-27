package com.n33.jcu.utils.locks.phaser;

import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;

/**
 * 停掉Phase
 *
 * @author N33
 * @date 2019/5/27
 */
public class PhaserRxample8 {


    public static void main(String[] args) throws InterruptedException {
        final Phaser phaser = new Phaser(3);

        new Thread(phaser::arriveAndAwaitAdvance).start();

        TimeUnit.SECONDS.sleep(1);

        System.out.println(phaser.isTerminated());

        //销毁Phase
        phaser.forceTermination();

        System.out.println(phaser.isTerminated());
    }

}
