package com.n33.jcu.utils.exchanger;

import java.util.concurrent.Exchanger;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 交换并改变数据
 *
 * @author N33
 * @date 2019/5/23
 */
public class ExChangeExample3 {


    public static void main(String[] args) {

        final Exchanger<Integer> exchanger = new Exchanger<>();

        new Thread(() -> {
            AtomicReference<Integer> value = new AtomicReference<>(1);

            try {
                while (true) {
                    value.set(exchanger.exchange(value.get()));
                    System.out.println("Thread A has value: " + value.get());
                    TimeUnit.SECONDS.sleep(3);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            AtomicReference<Integer> value = new AtomicReference<>(2);

            try {
                while (true) {
                    value.set(exchanger.exchange(value.get()));
                    System.out.println("Thread B has value: " + value.get());
                    TimeUnit.SECONDS.sleep(2);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
