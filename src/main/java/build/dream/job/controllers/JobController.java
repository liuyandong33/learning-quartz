package build.dream.job.controllers;

import build.dream.common.annotations.ApiRestAction;
import build.dream.common.api.ApiRest;
import build.dream.common.utils.ApplicationHandler;
import build.dream.common.utils.GsonUtils;
import build.dream.job.jobs.SimpleJob;
import build.dream.job.models.job.StartSimpleJobModel;
import build.dream.job.models.job.StopJobModel;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.Map;

@Controller
@RequestMapping(value = "/job")
public class JobController {
    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;

    @RequestMapping(value = "/stopJob", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiRestAction(error = "停止定时任失败")
    public String stopJob() throws Exception {
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        StopJobModel stopJobModel = ApplicationHandler.instantiateObject(StopJobModel.class, requestParameters);
        stopJobModel.validateAndThrow();

        String triggerName = stopJobModel.getTriggerName();
        String triggerGroup = stopJobModel.getTriggerGroup();
        String jobName = stopJobModel.getJobName();
        String jobGroup = stopJobModel.getJobGroup();

        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        TriggerKey triggerKey = TriggerKey.triggerKey(triggerName, triggerGroup);

        scheduler.pauseTrigger(triggerKey);
        scheduler.unscheduleJob(triggerKey);

        JobKey jobKey = JobKey.jobKey(jobName, jobGroup);
        scheduler.deleteJob(jobKey);

        return GsonUtils.toJson(ApiRest.builder().message("停止定时任务成功！").successful(true).build());
    }

    @RequestMapping(value = "/startSimpleJob", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiRestAction(error = "开始简单定时任务失败")
    public String startSimpleJob() throws Exception {
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        StartSimpleJobModel startSimpleJobModel = ApplicationHandler.instantiateObject(StartSimpleJobModel.class, requestParameters);
        startSimpleJobModel.validateAndThrow();

        String jobName = startSimpleJobModel.getJobName();
        String jobGroup = startSimpleJobModel.getJobGroup();
        String triggerName = startSimpleJobModel.getTriggerName();
        String triggerGroup = startSimpleJobModel.getTriggerGroup();
        Integer interval = startSimpleJobModel.getInterval();
        Integer repeatCount = startSimpleJobModel.getRepeatCount();
        Date startTime = startSimpleJobModel.getStartTime();
        Date endTime = startSimpleJobModel.getEndTime();
        String topic = startSimpleJobModel.getTopic();
        String data = startSimpleJobModel.getData();

        SimpleScheduleBuilder simpleScheduleBuilder = SimpleScheduleBuilder.simpleSchedule();
        if (interval != null) {
            simpleScheduleBuilder.withIntervalInSeconds(interval);
        }

        if (repeatCount != null) {
            simpleScheduleBuilder.withRepeatCount(repeatCount);
        }

        TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder.newTrigger();
        triggerBuilder.withIdentity(triggerName, triggerGroup);
        triggerBuilder.withSchedule(simpleScheduleBuilder);

        if (startTime != null) {
            triggerBuilder.startAt(startTime);
        }

        if (endTime != null) {
            triggerBuilder.endAt(endTime);
        }

        JobBuilder jobBuilder = JobBuilder.newJob(SimpleJob.class);
        jobBuilder.withIdentity(jobName, jobGroup);
        JobDetail jobDetail = jobBuilder.build();
        JobDataMap jobDataMap = jobDetail.getJobDataMap();
        jobDataMap.put("topic", topic);
        jobDataMap.put("data", data);

        Trigger trigger = triggerBuilder.build();

        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        scheduler.scheduleJob(jobDetail, trigger);
        return GsonUtils.toJson(ApiRest.builder().message("开始简单定时任务成功！").successful(true).build());
    }
}
