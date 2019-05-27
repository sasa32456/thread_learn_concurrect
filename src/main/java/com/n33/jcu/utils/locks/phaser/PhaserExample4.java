package com.n33.jcu.utils.locks.phaser;

import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;

/**
 * 测试Phaser会跑多少轮
 *
 * @author N33
 * @date 2019/5/27
 */
public class PhaserExample4 {

    public static void main(String[] args) throws InterruptedException {

//        final Phaser phaser = new Phaser(1);
//        System.out.println(phaser.getPhase());
//
//        phaser.arriveAndAwaitAdvance();
//        System.out.println(phaser.getPhase());
//
//        phaser.arriveAndAwaitAdvance();
//        System.out.println(phaser.getPhase());
//
//        phaser.arriveAndAwaitAdvance();
//        System.out.println(phaser.getPhase());


        //获取注册数目
//        System.out.println(phaser.getRegisteredParties());
//        phaser.register();
//
//        System.out.println(phaser.getRegisteredParties());
//
//        phaser.register();
//
//        System.out.println(phaser.getRegisteredParties());


        //那些是已经到达的
//        System.out.println(phaser.getArrivedParties());
//        System.out.println(phaser.getUnarrivedParties());

        //注册多个
//        phaser.bulkRegister(10);
//        System.out.println(phaser.getRegisteredParties());
//        System.out.println(phaser.getArrivedParties());
//        System.out.println(phaser.getUnarrivedParties());
//
//        new Thread(phaser::arriveAndAwaitAdvance).start();
//        TimeUnit.SECONDS.sleep(1);
//        System.out.println("======================");
//
//        System.out.println(phaser.getRegisteredParties());
//        System.out.println(phaser.getArrivedParties());
//        System.out.println(phaser.getUnarrivedParties());


        final Phaser phaser = new Phaser(2) {
            @Override
            protected boolean onAdvance(int phase, int registeredParties) {
                //true代表销毁
                return false;
            }
        };

        new OnAvanceTask("Alex", phaser).start();
        new OnAvanceTask("Jack", phaser).start();


        TimeUnit.SECONDS.sleep(2);

        System.out.println(phaser.getUnarrivedParties());
        System.out.println(phaser.getArrivedParties());

    }


    static class OnAvanceTask extends Thread {
        private final Phaser phaser;

        public OnAvanceTask(String name, Phaser phaser) {
            super(name);
            this.phaser = phaser;
        }

        @Override
        public void run() {
            System.out.println(getName() + " I am start and the phase " + phaser.getPhase());
            phaser.arriveAndAwaitAdvance();
            System.out.println(getName() + "I am end! ");

            System.out.println("isTerminated-" + phaser.isTerminated());

            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (getName().equals("Alex")) {
                System.out.println(getName() + " I am start and the phase " + phaser.getPhase());
                phaser.arriveAndAwaitAdvance();
                System.out.println(getName() + "I am end! ");
            }

            System.out.println("isTerminated-" + phaser.isTerminated());

        }
    }

}

/**
 * 可重写的方法，用于在即将发生相位超前时执行操作，并控制终止。
 * 在推进该移相器的一方到来时（当所有其他等待方都处于休眠状态时）调用该方法。
 * 如果此方法返回{@code true}，则此提升程序将在提前设置为最终终止状态，
 * 随后对{@link #isTerminated}的调用将返回true。
 * 通过调用此方法抛出的任何（未经检查的）异常或错误将传播到尝试推进此移相器的一方，在这种情况下不会发生进展。
 * <p>此方法的参数提供了当前转换的主要相位器状态。
 * 从{@code onAdvance}中调用此移相器的到达，注册和等待方法的效果未指定，不应依赖。
 * <p>如果此移相器是分层移相器的成员，则仅在每次前进时为其根移相器调用{@code onAdvance}。
 * <p>为了支持最常见的用例，当一方调用{@code arrivalAndDeregister}导致注册方数量变为零时，
 * 此方法的默认实现将返回{@code true}。您可以禁用此行为，从而在将来注册时启用，
 * 通过重写此方法始终返回{@code false}：
 *
 * <pre> {@code
 * Phaser phaser = new Phaser() {
 *   protected boolean onAdvance(int phase, int parties) { return false; }
 * }}</pre>
 *
 * @param phase the current phase number on entry to this method,
 * before this phaser is advanced
 * @param registeredParties the current number of registered parties
 * @return {@code true} if this phaser should terminate
 * <p>
 * protected boolean onAdvance(int phase, int registeredParties) {
 * return registeredParties == 0;
 * }
 */
