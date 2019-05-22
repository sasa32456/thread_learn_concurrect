package com.n33.jcu.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 任务之间互动
 * 高级
 * 反复学习
 *
 * @author N33
 * @date 2019/5/22
 */
public class CountDownLatchExample4 {

    private static Random random = new Random(System.currentTimeMillis());

    static class Event {
        int id = 0;

        public Event(int id) {
            this.id = id;
        }
    }

    /**
     * 没办法知道完成几项任务
     */
    interface Watcher {
//        void startWatch();

        void done(Table table);
    }

    static class TaskBatch implements Watcher {

        private CountDownLatch countDownLatch;

        private TaskGroup taskGroup;

        public TaskBatch(TaskGroup taskGroup, int size) {
            this.countDownLatch = new CountDownLatch(size);
            this.taskGroup = taskGroup;
        }

//        @Override
//        public void startWatch() {
//        }

        @Override
        public void done(Table table) {
            countDownLatch.countDown();
            if (countDownLatch.getCount() == 0) {
                System.out.println("The table " + table.tableName + " finished work,[" + table.toString() + "]");
                taskGroup.done(table);
            }
        }
    }


    static class TaskGroup implements Watcher {

        private CountDownLatch countDownLatch;

        private Event event;

        public TaskGroup(int size, Event event) {
            this.countDownLatch = new CountDownLatch(size);
            this.event = event;
        }


        @Override
        public void done(Table table) {
            countDownLatch.countDown();
            if (countDownLatch.getCount() == 0) {
                System.out.println("All of table done in event: " + event.id);
            }
        }
    }


    /**
     * 每一项都是个任务细化
     */
    static class Table {
        String tableName;
        long sourceRecordCount = 10;
        long targetCount;
        String sourceColumnSchema = "<table name='a'><column name='coll' type='varchar2' /></table>";
        String targetColumnSchema = "";

        public Table(String tableName, long sourceRecordCount) {
            this.tableName = tableName;
            this.sourceRecordCount = sourceRecordCount;
        }

        @Override
        public String toString() {
            return "Table{" +
                    "tableName='" + tableName + '\'' +
                    ", sourceRecordCount=" + sourceRecordCount +
                    ", targetCount=" + targetCount +
                    ", sourceColumnSchema='" + sourceColumnSchema + '\'' +
                    ", targetColumnSchema='" + targetColumnSchema + '\'' +
                    '}';
        }
    }

    private static List<Table> capture(Event event) {
        List<Table> list = new ArrayList<Table>();
        for (int i = 0; i < 10; i++) {
            list.add(new Table("table-" + event.id + "-" + i, i * 1000));
        }
        return list;
    }


    public static void main(String[] args) {

        final Event[] events = {new Event(1), new Event(2)};

        ExecutorService executorService = Executors.newFixedThreadPool(5);

        for (Event event : events) {
            List<Table> tables = capture(event);
            TaskGroup taskGroup = new TaskGroup(tables.size(), event);
            for (Table table : tables) {
                TaskBatch taskBatch = new TaskBatch(taskGroup,2 );
                final TrustSourceColumns columnsRunnable = new TrustSourceColumns(table, taskBatch);
                final TrustSourceRecordCount recordCountRunnable = new TrustSourceRecordCount(table, taskBatch);

                executorService.submit(columnsRunnable);
                executorService.submit(recordCountRunnable);


            }
        }


    }

    static class TrustSourceRecordCount implements Runnable {

        private final Table table;

        private TaskBatch taskBatch;

        public TrustSourceRecordCount(Table table, TaskBatch taskBatch) {
            this.table = table;
            this.taskBatch = taskBatch;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(random.nextInt(10000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            table.targetCount = table.sourceRecordCount;
            //System.out.println("The table " + table.tableName + " target record count capture done and update the data");
            taskBatch.done(table);
        }
    }


    static class TrustSourceColumns implements Runnable {

        private final Table table;

        private TaskBatch taskBatch;

        public TrustSourceColumns(Table table, TaskBatch taskBatch) {
            this.table = table;
            this.taskBatch = taskBatch;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(random.nextInt(10000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            table.targetColumnSchema = table.sourceColumnSchema;
            //System.out.println("The table " + table.tableName + " target colums capture done and update the data");

            taskBatch.done(table);
        }
    }

}
