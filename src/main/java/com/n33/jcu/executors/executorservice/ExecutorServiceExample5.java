package com.n33.jcu.executors.executorservice;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Queue的取出
 *
 * @author N33
 * @date 2019/6/2
 */
public class ExecutorServiceExample5 {


    public static void main(String[] args) throws InterruptedException {

        ThreadPoolExecutor executorService = (ThreadPoolExecutor) Executors.newFixedThreadPool(5);

//        //先启动线程执行
//        executorService.execute(() -> System.out.println("I will be process because of triggered the executue."));
//        TimeUnit.SECONDS.sleep(2);

        //不执行
        executorService.getQueue().add(() -> System.out.println("I am added directly into the queue."));




    }
}
