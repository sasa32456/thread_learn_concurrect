package com.n33.jcu.executors.scheduler;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import static org.quartz.CronScheduleBuilder.*;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.*;

/**
 * Quartz不会被长任务阻塞
 *
 * @author N33
 * @date 2019/5/30
 */
public class QuartzSchedule {

    public static void main(String[] args) throws SchedulerException {

        JobDetail job = newJob(SimpleJob.class).withIdentity("Job1", "Group1").build();

        Trigger trigger = newTrigger().withIdentity("trigger1", "group1").
                withSchedule(cronSchedule("0/5 * * * * ?")).build();

        Scheduler scheduler = new StdSchedulerFactory().getScheduler();

        scheduler.start();
        scheduler.scheduleJob(job, trigger);
    }

}
