package com.n33.jcu.executors.scheduler;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 * 老掉牙定时
 *
 * @author N33
 * @date 2019/5/29
 */
public class TimerSchedler {

    /**
     * scheduler solution
     * Timer/TimerTask
     * SchedulerExecutorService
     * crontab linux上
     * cron4j
     * quartz
     *
     * Control-M商业
     *
     * @param args
     */
    public static void main(String[] args) {
        /**
         * Timer:
         * Question
         * when the timertask process more than 1 seconds what happen?
         * 会被阻塞
         */
        Timer timer = new Timer();
        final TimerTask timerTask = new TimerTask(){
            @Override
            public void run() {
                System.out.println("===" + System.currentTimeMillis());
                try {
                    TimeUnit.SECONDS.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        timer.schedule(timerTask, 1000,1000);

    }
}
