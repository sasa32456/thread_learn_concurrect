package com.n33.jcu.collections.cuncurrent.jdkmapperformance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 对比
 * ConcurrentHashMap
 * Count: 10 ,ms: 144
 * Count: 20 ,ms: 100
 * Count: 30 ,ms: 142
 * Count: 40 ,ms: 141
 * Count: 50 ,ms: 138
 * Count: 60 ,ms: 141
 * Count: 70 ,ms: 147
 * Count: 80 ,ms: 147
 * Count: 90 ,ms: 152
 * ============================
 * ConcurrentSkipListMap
 * Count: 10 ,ms: 242
 * Count: 20 ,ms: 246
 * Count: 30 ,ms: 205
 * Count: 40 ,ms: 204
 * Count: 50 ,ms: 205
 * Count: 60 ,ms: 213
 * Count: 70 ,ms: 210
 * Count: 80 ,ms: 222
 * Count: 90 ,ms: 213
 * ============================
 *
 * @author N33
 * @date 2019/6/19
 */
public class ConcurrentSkipListMapVsConcurrentHashMap {


    static class Entry{
        int threshold;
        long ms;

        public Entry(int threshold, long ms) {
            this.threshold = threshold;
            this.ms = ms;
        }

        @Override
        public String toString() {
            return "Count: " + threshold + " ,ms: "+ms;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        for (int i = 10; i < 100; i+= 10) {
            pressureTest(new ConcurrentHashMap<>(), i);
            pressureTest(new ConcurrentSkipListMap<>(), i);
        }

        summary.forEach((k,v)->{
            System.out.println(k.getSimpleName());
            v.forEach(System.out::println);
            System.out.println("============================");
        });
    }


    private final static Map<Class<?>, List<Entry>> summary = new HashMap<Class<?>, List<Entry>>(){
        {
            put(ConcurrentHashMap.class, new ArrayList<>());
            put(ConcurrentSkipListMap.class, new ArrayList<>());

        }
    };


    private static void pressureTest(final Map<String, Integer> map, int threshold) throws InterruptedException {
        System.out.println("Start pressure testing the map [" + map.getClass() + "] use the threshold [" + threshold + "]");
        long totalTime = 0;
        final int MAX_THRESHOLD = 500000;
        for (int i = 0; i < 5; i++) {
            final AtomicInteger counter = new AtomicInteger(0);
            map.clear();
            long startTime = System.nanoTime();
            final ExecutorService executorService = Executors.newFixedThreadPool(threshold);
            for (int j = 0; j < threshold; j++) {
                executorService.execute(() -> {
                    for (int k = 0; k < MAX_THRESHOLD&&counter.getAndIncrement()<MAX_THRESHOLD; k++) {
                        Integer randomNumber = (int) Math.ceil(Math.random() * 600000);
                        map.get(String.valueOf(randomNumber));
                        map.put(String.valueOf(randomNumber), randomNumber);
                    }
                });
            }
            executorService.shutdown();
            executorService.awaitTermination(2, TimeUnit.HOURS);
            long endTime = System.nanoTime();
            long period = (endTime - startTime) / 1000000L;
            System.out.println(threshold * MAX_THRESHOLD + " element inserted/retrieved in " + period + " ms");
            totalTime += period;
        }
        summary.get(map.getClass()).add(new Entry(threshold, (totalTime / 5)));
        System.out.println("For the map [" + map.getClass() + "] the average time is " + (totalTime / 5) + " ms");
    }


}
