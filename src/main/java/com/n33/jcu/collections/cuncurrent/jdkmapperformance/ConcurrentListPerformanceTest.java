package com.n33.jcu.collections.cuncurrent.jdkmapperformance;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 性能对比
 * SynchronizedRandomAccessList
 * Count: 10 ,ms: 3
 * Count: 20 ,ms: 2
 * Count: 30 ,ms: 3
 * Count: 40 ,ms: 5
 * Count: 50 ,ms: 8
 * Count: 60 ,ms: 7
 * Count: 70 ,ms: 7
 * Count: 80 ,ms: 9
 * Count: 90 ,ms: 10
 * Count: 100 ,ms: 12
 * ============================
 * ConcurrentLinkedQueue
 * Count: 10 ,ms: 14
 * Count: 20 ,ms: 3
 * Count: 30 ,ms: 4
 * Count: 40 ,ms: 5
 * Count: 50 ,ms: 7
 * Count: 60 ,ms: 8
 * Count: 70 ,ms: 9
 * Count: 80 ,ms: 8
 * Count: 90 ,ms: 12
 * Count: 100 ,ms: 14
 * ============================
 * CopyOnWriteArrayList
 * Count: 10 ,ms: 57
 * Count: 20 ,ms: 48
 * Count: 30 ,ms: 53
 * Count: 40 ,ms: 50
 * Count: 50 ,ms: 49
 * Count: 60 ,ms: 53
 * Count: 70 ,ms: 42
 * Count: 80 ,ms: 40
 * Count: 90 ,ms: 40
 * Count: 100 ,ms: 48
 * ============================
 *
 * @author N33
 * @date 2019/6/20
 */
public class ConcurrentListPerformanceTest {


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

    public static void main(String[] args) throws Exception {
        for (int i = 10; i <= 100; i += 10) {
            pressureTest(new ConcurrentLinkedQueue<>(), i);
            pressureTest(new CopyOnWriteArrayList<>(), i);
            pressureTest(Collections.synchronizedList(new ArrayList<>()), i);
        }


        summary.forEach((k,v)->{
            System.out.println(k);
            v.forEach(System.out::println);
            System.out.println("============================");
        });
    }


    private final static Map<String, List<Entry>> summary = new HashMap<>();


    private static void pressureTest(final Collection<String> list, int threshold) throws InterruptedException {
        System.out.println("Start pressure testing the list [" + list.getClass() + "] use the threshold [" + threshold + "]");
        long totalTime = 0;
        final int MAX_THRESHOLD = 10000;
        for (int i = 0; i < 5; i++) {
            final AtomicInteger counter = new AtomicInteger(0);
            list.clear();
            long startTime = System.nanoTime();
            final ExecutorService executorService = Executors.newFixedThreadPool(threshold);
            for (int j = 0; j < threshold; j++) {
                executorService.execute(() -> {
                    for (int k = 0; k < MAX_THRESHOLD&&counter.getAndIncrement()<MAX_THRESHOLD; k++) {
                        Integer randomNumber = (int) Math.ceil(Math.random() * 600000);
                        list.add(String.valueOf(randomNumber));
                    }
                });
            }
            executorService.shutdown();
            executorService.awaitTermination(2, TimeUnit.HOURS);
            long endTime = System.nanoTime();
            long period = (endTime - startTime) / 1000000L;
            System.out.println(threshold * MAX_THRESHOLD + " element add in " + period + " ms");
            totalTime += period;
        }
        List<Entry> entries = summary.get(list.getClass().getSimpleName());
        if (entries == null) {
            entries = new ArrayList<>();
            summary.put(list.getClass().getSimpleName(), entries);
        }
        entries.add(new Entry(threshold, (totalTime / 5)));
        System.out.println("For the list [" + list.getClass() + "] the average time is " + (totalTime / 5) + " ms");
    }
}
