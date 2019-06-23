package com.n33.jcu.collections.cuncurrent.jdkmapperformance;

import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Map压力测试
 *
 * @author N33
 * @date 2019/6/17
 */
public class JdkMapPerformance {

    /**
     * Concurrency Testing use five threads
     *
     * <pre>
     * Start pressure testing the map [class java.util.Hashtable] use the threshold [5],retrieve=false
     * 2500000 element inserted/retrieved in 1082 ms
     * 2500000 element inserted/retrieved in 1008 ms
     * 2500000 element inserted/retrieved in 855 ms
     * 2500000 element inserted/retrieved in 889 ms
     * 2500000 element inserted/retrieved in 886 ms
     * For the map [class java.util.Hashtable] the average time is 944 ms
     * Start pressure testing the map [class java.util.Hashtable] use the threshold [5],retrieve=true
     * 2500000 element inserted/retrieved in 1511 ms
     * 2500000 element inserted/retrieved in 1572 ms
     * 2500000 element inserted/retrieved in 1363 ms
     * 2500000 element inserted/retrieved in 1467 ms
     * 2500000 element inserted/retrieved in 1460 ms
     * For the map [class java.util.Hashtable] the average time is 1474 ms
     * Start pressure testing the map [class java.util.Collections$SynchronizedMap] use the threshold [5],retrieve=false
     * 2500000 element inserted/retrieved in 984 ms
     * 2500000 element inserted/retrieved in 973 ms
     * 2500000 element inserted/retrieved in 981 ms
     * 2500000 element inserted/retrieved in 909 ms
     * 2500000 element inserted/retrieved in 893 ms
     * For the map [class java.util.Collections$SynchronizedMap] the average time is 948 ms
     * Start pressure testing the map [class java.util.Collections$SynchronizedMap] use the threshold [5],retrieve=true
     * 2500000 element inserted/retrieved in 1410 ms
     * 2500000 element inserted/retrieved in 1473 ms
     * 2500000 element inserted/retrieved in 1382 ms
     * 2500000 element inserted/retrieved in 1409 ms
     * 2500000 element inserted/retrieved in 1400 ms
     * For the map [class java.util.Collections$SynchronizedMap] the average time is 1414 ms
     * Start pressure testing the map [class java.util.concurrent.ConcurrentHashMap] use the threshold [5],retrieve=false
     * 2500000 element inserted/retrieved in 520 ms
     * 2500000 element inserted/retrieved in 417 ms
     * 2500000 element inserted/retrieved in 392 ms
     * 2500000 element inserted/retrieved in 427 ms
     * 2500000 element inserted/retrieved in 419 ms
     * For the map [class java.util.concurrent.ConcurrentHashMap] the average time is 435 ms
     * Start pressure testing the map [class java.util.concurrent.ConcurrentHashMap] use the threshold [5],retrieve=true
     * 2500000 element inserted/retrieved in 431 ms
     * 2500000 element inserted/retrieved in 334 ms
     * 2500000 element inserted/retrieved in 335 ms
     * 2500000 element inserted/retrieved in 332 ms
     * 2500000 element inserted/retrieved in 634 ms
     * For the map [class java.util.concurrent.ConcurrentHashMap] the average time is 413 ms
     * </pre>
     * <p>
     * Single thread testing report
     * <p>
     * Start pressure testing the map [class java.util.Hashtable] use the threshold [1],retrieve=false
     * 2500000 element inserted/retrieved in 966 ms
     * 2500000 element inserted/retrieved in 892 ms
     * 2500000 element inserted/retrieved in 670 ms
     * 2500000 element inserted/retrieved in 768 ms
     * 2500000 element inserted/retrieved in 697 ms
     * For the map [class java.util.Hashtable] the average time is 798 ms
     * Start pressure testing the map [class java.util.Hashtable] use the threshold [1],retrieve=true
     * 2500000 element inserted/retrieved in 1018 ms
     * 2500000 element inserted/retrieved in 1072 ms
     * 2500000 element inserted/retrieved in 951 ms
     * 2500000 element inserted/retrieved in 988 ms
     * 2500000 element inserted/retrieved in 949 ms
     * For the map [class java.util.Hashtable] the average time is 995 ms
     * Start pressure testing the map [class java.util.Collections$SynchronizedMap] use the threshold [1],retrieve=false
     * 2500000 element inserted/retrieved in 800 ms
     * 2500000 element inserted/retrieved in 836 ms
     * 2500000 element inserted/retrieved in 852 ms
     * 2500000 element inserted/retrieved in 724 ms
     * 2500000 element inserted/retrieved in 733 ms
     * For the map [class java.util.Collections$SynchronizedMap] the average time is 789 ms
     * Start pressure testing the map [class java.util.Collections$SynchronizedMap] use the threshold [1],retrieve=true
     * 2500000 element inserted/retrieved in 870 ms
     * 2500000 element inserted/retrieved in 960 ms
     * 2500000 element inserted/retrieved in 762 ms
     * 2500000 element inserted/retrieved in 773 ms
     * 2500000 element inserted/retrieved in 800 ms
     * For the map [class java.util.Collections$SynchronizedMap] the average time is 833 ms
     * Start pressure testing the map [class java.util.concurrent.ConcurrentHashMap] use the threshold [1],retrieve=false
     * 2500000 element inserted/retrieved in 804 ms
     * 2500000 element inserted/retrieved in 678 ms
     * 2500000 element inserted/retrieved in 686 ms
     * 2500000 element inserted/retrieved in 687 ms
     * 2500000 element inserted/retrieved in 656 ms
     * For the map [class java.util.concurrent.ConcurrentHashMap] the average time is 702 ms
     * Start pressure testing the map [class java.util.concurrent.ConcurrentHashMap] use the threshold [1],retrieve=true
     * 2500000 element inserted/retrieved in 926 ms
     * 2500000 element inserted/retrieved in 994 ms
     * 2500000 element inserted/retrieved in 907 ms
     * 2500000 element inserted/retrieved in 865 ms
     * 2500000 element inserted/retrieved in 862 ms
     * For the map [class java.util.concurrent.ConcurrentHashMap] the average time is 910 ms
     * Start pressure testing the map [class java.util.HashMap] use the threshold [1],retrieve=false
     * 2500000 element inserted/retrieved in 698 ms
     * 2500000 element inserted/retrieved in 840 ms
     * 2500000 element inserted/retrieved in 755 ms
     * 2500000 element inserted/retrieved in 794 ms
     * 2500000 element inserted/retrieved in 799 ms
     * For the map [class java.util.HashMap] the average time is 777 ms
     * Start pressure testing the map [class java.util.HashMap] use the threshold [1],retrieve=true
     * 2500000 element inserted/retrieved in 934 ms
     * 2500000 element inserted/retrieved in 839 ms
     * 2500000 element inserted/retrieved in 863 ms
     * 2500000 element inserted/retrieved in 933 ms
     * 2500000 element inserted/retrieved in 1228 ms
     * For the map [class java.util.HashMap] the average time is 959 ms
     *
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
//        pressureTest(new Hashtable<String, Integer>(), 5, false);
//        pressureTest(new Hashtable<String, Integer>(), 5, true);
//
//        pressureTest(Collections.synchronizedMap(new HashMap<>()), 5, false);
//        pressureTest(Collections.synchronizedMap(new HashMap<>()), 5, true);
//
//        pressureTest(new ConcurrentHashMap<>(), 5, false);
//        pressureTest(new ConcurrentHashMap<>(), 5, true);
        System.out.println("==========================================");

        pressureTest(new Hashtable<String, Integer>(), 1, false);
        pressureTest(new Hashtable<String, Integer>(), 1, true);

        pressureTest(Collections.synchronizedMap(new HashMap<>()), 1, false);
        pressureTest(Collections.synchronizedMap(new HashMap<>()), 1, true);

        pressureTest(new ConcurrentHashMap<>(), 1, false);
        pressureTest(new ConcurrentHashMap<>(), 1, true);

        pressureTest(new HashMap<>(), 1, false);
        pressureTest(new HashMap<>(), 1, true);
    }


    private static void pressureTest(final Map<String, Integer> map, int threshold, boolean retrieve) throws InterruptedException {
        System.out.println("Start pressure testing the map [" + map.getClass() + "] use the threshold [" + threshold + "],retrieve=" + retrieve);
        long totalTime = 0;
//        final int MAX_THRESHOLD = 500000;
        final int MAX_THRESHOLD = 2500000;
        for (int i = 0; i < 5; i++) {
            long startTime = System.nanoTime();
            final ExecutorService executorService = Executors.newFixedThreadPool(threshold);
            for (int j = 0; j < threshold; j++) {
                executorService.execute(() -> {
                    for (int k = 0; k < MAX_THRESHOLD; k++) {
                        Integer randomNumber = (int) Math.ceil(Math.random() * 600000);
                        if (retrieve) map.get(String.valueOf(randomNumber));
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
        System.out.println("For the map [" + map.getClass() + "] the average time is " + (totalTime / 5) + " ms");
    }


}
