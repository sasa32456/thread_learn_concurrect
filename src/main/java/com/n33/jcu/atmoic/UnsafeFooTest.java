package com.n33.jcu.atmoic;

import sun.misc.Unsafe;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;

/**
 * Unsafe绕过初始化
 *
 * @author N33
 * @date 2019/5/21
 */
public class UnsafeFooTest {


    private static Unsafe getUnsafe() {
        try {
            Field f = Unsafe.class.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            return (Unsafe) f.get(null);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    public static void main(String[] args) throws Exception {
//        final Simple simple = new Simple();
//        System.out.println(simple.get());
//        final Simple simple = Simple.class.newInstance();

        //反射不会走初始化方法
//        final Class<?> aClass = Class.forName("com.n33.jcu.atmoic.UnsafeFooTest$Simple");

        //Unsafe绕过初始化
//        Unsafe unsafe = getUnsafe();
        //直接开辟内存
//        final Simple simple = (Simple) unsafe.allocateInstance(Simple.class);
//        System.out.println(simple.get());
        /**
         * class com.n33.jcu.atmoic.UnsafeFooTest$Simple
         * sun.misc.Launcher$AppClassLoader@18b4aac2
         * 只是不初始化
         */
//        System.out.println(simple.getClass());
//        System.out.println(simple.getClass().getClassLoader());

//        final Guard guard = new Guard();
//        guard.work();
//
//        final Field f = guard.getClass().getDeclaredField("ACCESS_ALLOWED");
////        unsafe破解权限
//        unsafe.putInt(guard, unsafe.objectFieldOffset(f), 42);
//        guard.work();

        /**
         * Unsafe加载class
         */
//        final byte[] bytes = loadClassContent();
//        final Class<?> aClass = unsafe.defineClass(null, bytes, 0, bytes.length, null, null);
//        final int v = (Integer)aClass.getMethod("get").invoke(aClass.newInstance(), null);
//        System.out.println(v);

        System.out.println(sizeof(new Simple()));
    }

    /**
     * 获取Obj的size
     * @param obj
     * @return
     */
    private static long sizeof(Object obj) {
        Unsafe unsafe = getUnsafe();
        Set<Field> fields = new HashSet<>();
        Class<?> c = obj.getClass();
        while (c != Object.class) {
            final Field[] declaredFields = c.getDeclaredFields();
            for (Field f : declaredFields) {
                if ((f.getModifiers() & Modifier.STATIC) == 0) {
                    fields.add(f);
                }
            }
            c = c.getSuperclass();
        }
        long maxoffSet = 0;
        for (Field f : fields) {
            long offSet = unsafe.objectFieldOffset(f);
            if (offSet > maxoffSet) {
                maxoffSet = offSet;
            }
        }

        return ((maxoffSet / 8) + 1) * 8;
    }


    private static byte[] loadClassContent() throws IOException {
        File f = new File("F:\\IdeaProjectsLearn\\zzz\\A.class");
        FileInputStream fis = new FileInputStream(f);
        byte[] content = new byte[(int) f.length()];
        fis.read(content);
        fis.close();
        return content;
    }

    static class Guard {
        private int ACCESS_ALLOWED = 1;

        public boolean allow() {
            return 42 == ACCESS_ALLOWED;
        }

        public void work() {
            if (allow()) {
                System.out.println("I am working by allowed");
            }
        }
    }


    static class Simple {
        private long l = 0;
        private int i = 10;
        private byte b = (byte) 0x01;

        public Simple() {
            this.l = 1;
            System.out.println("=============");
        }

        public long get() {
            return l;
        }
    }


}
