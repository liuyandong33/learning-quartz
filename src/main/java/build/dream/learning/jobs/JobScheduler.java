package build.dream.learning.jobs;

import org.apache.commons.lang.StringUtils;
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
        scheduler.clear();
        String dataJobCronExpression = "*/5 * * * * ?";
        dataJobCronExpression = null;
        if (StringUtils.isNotBlank(dataJobCronExpression)) {
            JobDetail dataJobDetail = JobBuilder.newJob(DataJob.class).withIdentity("dataJob", "cateringJobGroup").build();
            CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(dataJobCronExpression);
            Trigger dataJobCronTrigger = TriggerBuilder.newTrigger().withIdentity("dataJobTrigger", "cateringJobGroup").withSchedule(cronScheduleBuilder).build();
            scheduler.scheduleJob(dataJobDetail, dataJobCronTrigger);
        }
    }
}
