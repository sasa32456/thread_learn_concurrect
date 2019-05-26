package com.n33.jcu.utils.locks.forkjoin;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import java.util.stream.IntStream;

/**
 * 分而治之,不断拆分，利用多线程计算，再汇聚
 * 累加
 *
 * @author N33
 * @date 2019/5/26
 */
public class ForkJoinRecutsiceTask {

    private final static int MAX_THRESHOLD = 3;


    public static void main(String[] args) {
        final ForkJoinPool forkJoinPool = new ForkJoinPool();
        final ForkJoinTask<Integer> future = forkJoinPool.submit(new CalulatedRecursiveTask(0, 1000));
        try {
            final Integer result = future.get();
            System.out.println(result);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }


    private static class CalulatedRecursiveTask extends RecursiveTask<Integer> {

        private final int start;

        private final int end;

        public CalulatedRecursiveTask(int start, int end) {
            this.start = start;
            this.end = end;
        }

        @Override
        protected Integer compute() {

            if (end - start <= MAX_THRESHOLD) {
                return IntStream.rangeClosed(start, end).sum();
            } else {
                int middle = (start + end) / 2;
                CalulatedRecursiveTask leftTask = new CalulatedRecursiveTask(start, middle);
                CalulatedRecursiveTask rightTask = new CalulatedRecursiveTask(middle+1, end);

                //异步计算
                leftTask.fork();
                rightTask.fork();
                //返回结果
                return leftTask.join() + rightTask.join();
            }
        }
    }

}

