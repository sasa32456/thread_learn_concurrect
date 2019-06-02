package com.n33.jcu.executors.future.completionservice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

/**
 * CompletionService
 *
 * @author N33
 * @date 2019/6/2
 */
public class CompletionServiceExample2 {

    public static void main(String[] args) throws Exception {
//        testCompletionService1();
        testCompletionService2();

    }

    private static void testCompletionService1() throws InterruptedException, ExecutionException {
        final ExecutorService service = Executors.newFixedThreadPool(2);

        final List<Callable<Integer>> callableList = Arrays.asList(
                () -> {
                    sleep(2);
                    System.out.println("The 2 finished.");
                    return 2;
                },
                () -> {
                    sleep(5);
                    System.out.println("The 5 finished.");
                    return 5;
                }
        );

        final ExecutorCompletionService<Integer> completionService = new ExecutorCompletionService<>(service);

        List<Future<Integer>> futures = new ArrayList<>();

        callableList.stream().forEach(callable-> futures.add(completionService.submit(callable)));

//        Future<Integer> future;
//        //take会阻塞
//        while ((future = completionService.take()) != null) {
//            System.out.println(future.get());
//        }


//        //可能还没执行好，拿到null
//        Future<Integer> future = completionService.poll();
//        System.out.println(future);


        System.out.println(completionService.poll(3, TimeUnit.SECONDS).get());
    }


    /**
     * runnable返回值
     * @throws Exception
     */
    private static void testCompletionService2() throws Exception {
        ExecutorService service = Executors.newFixedThreadPool(2);

        ExecutorCompletionService<Event> completionService = new ExecutorCompletionService<>(service);

        final Event event = new Event(1);

        completionService.submit(new MyTask(event), event);

        System.out.println(completionService.take().get().result);

    }


    private static class MyTask implements Runnable {

        private final Event event;

        public MyTask(Event event) {
            this.event = event;
        }

        @Override
        public void run() {
            sleep(2);
            event.setResult("I AM SUCCESSFULLY");
        }
    }



    private static class Event {
        final private int eventId;
        private String result;

        public Event(int eventId) {
            this.eventId = eventId;
        }

        public int getEventId() {
            return eventId;
        }

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }
    }










    private static void sleep(long seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
/**
 * 一个{@link CompletionService}，它使用提供的{@link Executor}来执行任务。
 * 本课程安排提交的任务在完成后放在可使用{@code take}访问的队列中。该类足够轻巧，适合在处理任务组时进行临时使用。
 *
 * <p>
 *
 * <b>Usage Examples.</b>
 *
 * 假设您有一组针对某个问题的求解器，每个求解器返回某种类型的值{@code Result}，
 * 并希望同时运行它们，处理每个返回非空值的结果，在某些情况下方法{@code use（Result r）}。你可以这样写：
 *
 * <pre> {@code
 * void solve(Executor e,
 *            Collection<Callable<Result>> solvers)
 *     throws InterruptedException, ExecutionException {
 *     CompletionService<Result> ecs
 *         = new ExecutorCompletionService<Result>(e);
 *     for (Callable<Result> s : solvers)
 *         ecs.submit(s);
 *     int n = solvers.size();
 *     for (int i = 0; i < n; ++i) {
 *         Result r = ecs.take().get();
 *         if (r != null)
 *             use(r);
 *     }
 * }}</pre>
 *
 * 相反，假设您希望使用任务集的第一个非null结果，忽略任何遇到异常，并在第一个任务准备就绪时取消所有其他任务：
 *
 * <pre> {@code
 * void solve(Executor e,
 *            Collection<Callable<Result>> solvers)
 *     throws InterruptedException {
 *     CompletionService<Result> ecs
 *         = new ExecutorCompletionService<Result>(e);
 *     int n = solvers.size();
 *     List<Future<Result>> futures
 *         = new ArrayList<Future<Result>>(n);
 *     Result result = null;
 *     try {
 *         for (Callable<Result> s : solvers)
 *             futures.add(ecs.submit(s));
 *         for (int i = 0; i < n; ++i) {
 *             try {
 *                 Result r = ecs.take().get();
 *                 if (r != null) {
 *                     result = r;
 *                     break;
 *                 }
 *             } catch (ExecutionException ignore) {}
 *         }
 *     }
 *     finally {
 *         for (Future<Result> f : futures)
 *             f.cancel(true);
 *     }
 *
 *     if (result != null)
 *         use(result);
 * }}</pre>
 *
 *
 * public class ExecutorCompletionService<V> implements CompletionService<V> {}
 */
