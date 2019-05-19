package com.n33.jcu.atmoic;

import javax.swing.*;
import java.util.concurrent.atomic.AtomicReference;
/**
 * 对其他类进行原子封装
 *
 * @author N33
 * @date 2019/5/19
 */
public class AtomicReferenceTest {

    public static void main(String[] args) {

        AtomicReference<Simple> atomic = new AtomicReference<Simple>(new Simple("Alex",12));

        System.out.println(atomic.get());

        final boolean result = atomic.compareAndSet(new Simple("sdfs", 22), new Simple("sdfs", 22));
        System.out.println(result);


        JButton button = new JButton();
        //default
        Simple s = new Simple("test", 12);
        button.addActionListener(e -> {
            //invoke restful service
            //s = new Simple("sdfs", 24);//需要final

            /**
             * 不需要final
             */
            atomic.set(new Simple("qwe", 1));

        });

    }

    static class Simple {

        private String name;
        private int age;


        public Simple(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }
    }

}
