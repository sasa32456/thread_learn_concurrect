package com.n33.jcu.utils.block.exchanger;

import java.util.concurrent.Exchanger;

/**
 * exChange价换的是对象（接收和拿到是同一个）
 *
 * @author N33
 * @date 2019/5/23
 */
public class ExChangeExample2 {


    /**
     * Actor 拷贝，拿到的发生改变不会对未拿到的影响
     * @param args
     */
    public static void main(String[] args) {

        final Exchanger<Object> exchanger = new Exchanger<>();

        new Thread(() -> {
            Object aobj = new Object();
            System.out.println("A will send the object " + aobj);

            try {
                final Object rObj = exchanger.exchange(aobj);
                System.out.println("A recieved the object " + rObj);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            Object bobj = new Object();
            System.out.println("B will send the object " + bobj);

            try {
                final Object rObj = exchanger.exchange(bobj);
                System.out.println("B recieved the object " + rObj);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();


    }
}
