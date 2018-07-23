package build.dream.learning.controllers;

import build.dream.common.annotations.ApiRestAction;
import build.dream.common.api.ApiRest;
import build.dream.common.constants.Constants;
import build.dream.common.utils.ApplicationHandler;
import build.dream.common.utils.GsonUtils;
import build.dream.learning.classloaders.JobClassLoader;
import build.dream.learning.jobs.CustomJob;
import build.dream.learning.models.job.StartSimpleJobModel;
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

    @RequestMapping(value = "/startSimpleJob", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiRestAction(error = "开始简单定时任务失败")
    public String startSimpleJob() throws Exception {
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        StartSimpleJobModel startSimpleJobModel = ApplicationHandler.instantiateObject(StartSimpleJobModel.class, requestParameters);
        startSimpleJobModel.validateAndThrow();

        String jobName = startSimpleJobModel.getJobName();
        String jobGroup = startSimpleJobModel.getJobName();
        Long interval = startSimpleJobModel.getInterval();
        Integer repeatCount = startSimpleJobModel.getRepeatCount();
        Date startTime = startSimpleJobModel.getStartTime();
        Date endTime = startSimpleJobModel.getEndTime();
        String triggerName = startSimpleJobModel.getTriggerName();
        String triggerGroup = startSimpleJobModel.getTriggerGroup();
        String jobClassName = startSimpleJobModel.getJobClassName();
        String jobClassBase64 = startSimpleJobModel.getJobClassBase64();


        Scheduler scheduler = schedulerFactoryBean.getScheduler();

        JobClassLoader jobClassLoader = new JobClassLoader();
        jobClassLoader.setClassBase64(jobClassBase64);

        Class<Job> jobClass = (Class<Job>) Class.forName(jobClassName, true, jobClassLoader);
        CustomJob.CONCURRENT_HASH_MAP.put(jobGroup + "_" + jobName, jobClass.newInstance());

        JobBuilder jobBuilder = JobBuilder.newJob(CustomJob.class);
        jobBuilder.withIdentity(jobName, jobGroup);
        JobDetail dataJobDetail = jobBuilder.build();

        SimpleScheduleBuilder simpleScheduleBuilder = SimpleScheduleBuilder.simpleSchedule();
        if (repeatCount != null) {
            simpleScheduleBuilder.withRepeatCount(repeatCount);
        }
        if (interval != null) {
            simpleScheduleBuilder.withIntervalInMilliseconds(interval);
        }

        TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder.newTrigger();
        triggerBuilder.withIdentity(triggerName, triggerGroup);
        if (startTime != null) {
            triggerBuilder.startAt(startTime);
        }
        if (endTime != null) {
            triggerBuilder.endAt(endTime);
        }
        triggerBuilder.withSchedule(simpleScheduleBuilder);

        Trigger trigger = triggerBuilder.build();
        scheduler.scheduleJob(dataJobDetail, trigger);
        return GsonUtils.toJson(ApiRest.builder().message("开始简单定时任务成功！").successful(true).build());
    }

    @RequestMapping(value = "/stopJob", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String stopJob() throws SchedulerException {
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        String orderCode = requestParameters.get("orderCode");
        ApplicationHandler.notBlank(orderCode, "orderCode");

        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        TriggerKey triggerKey = TriggerKey.triggerKey(orderCode, "cateringJobGroup");

        scheduler.pauseTrigger(triggerKey);
        scheduler.unscheduleJob(triggerKey);

        JobKey jobKey = JobKey.jobKey(orderCode, "cateringJobGroup");
        scheduler.deleteJob(jobKey);

        return Constants.SUCCESS;
    }
}
