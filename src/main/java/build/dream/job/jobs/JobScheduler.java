package build.dream.job.jobs;

import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;

@Component
public class JobScheduler {
    @Autowired
    SchedulerFactoryBean schedulerFactoryBean;

    public void scheduler() throws SchedulerException {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();

        JobKey jobKey = JobKey.jobKey("TEST_JOB", "DEFAULT_GROUP");
        TriggerKey triggerKey = TriggerKey.triggerKey("TEST_JOB_TRIGGER", "DEFAULT_GROUP");
        String cronExpression = "*/3 * * * * ?";

        if (scheduler.checkExists(jobKey) && scheduler.checkExists(triggerKey)) {
            CronTrigger cronTrigger = (CronTrigger) scheduler.getTrigger(triggerKey);

            if (!cronExpression.equals(cronTrigger.getCronExpression())) {
                cronTrigger.getCronExpression();
                scheduler.pauseTrigger(triggerKey);
                scheduler.unscheduleJob(triggerKey);
                scheduler.deleteJob(jobKey);

                scheduler(scheduler, TestJob.class, jobKey, triggerKey, cronExpression);
            }
        } else {
            scheduler(scheduler, TestJob.class, jobKey, triggerKey, cronExpression);
        }
    }

    public void scheduler(Scheduler scheduler, Class<? extends Job> jobClass, JobKey jobKey, TriggerKey triggerKey, String cronExpression) throws SchedulerException {
        JobDetail dataJobDetail = JobBuilder.newJob(jobClass).withIdentity(jobKey).build();
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);
        Trigger dataJobCronTrigger = TriggerBuilder.newTrigger().withIdentity(triggerKey).withSchedule(cronScheduleBuilder).build();
        scheduler.scheduleJob(dataJobDetail, dataJobCronTrigger);
    }
}
