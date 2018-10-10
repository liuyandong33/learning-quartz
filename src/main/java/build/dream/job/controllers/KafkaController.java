package build.dream.job.controllers;

import build.dream.common.annotations.ApiRestAction;
import build.dream.common.api.ApiRest;
import build.dream.common.constants.Constants;
import build.dream.common.utils.ApplicationHandler;
import build.dream.common.utils.ConfigurationUtils;
import build.dream.common.utils.GsonUtils;
import build.dream.job.jobs.KafkaJob;
import build.dream.job.models.kafka.CancelFixedTimeSendModel;
import build.dream.job.models.kafka.FixedTimeSendModel;
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
@RequestMapping(value = "/kafka")
public class KafkaController {
    private static final String PARTITION_CODE = ConfigurationUtils.getConfiguration(Constants.PARTITION_CODE);
    private static final String KAFKA_DELAY_SEND_JOB_GROUP = PARTITION_CODE + "_fixed_time_send";
    private static final String KAFKA_DELAY_SEND_TRIGGER_GROUP = PARTITION_CODE + "_fixed_time_send";
    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;

    @RequestMapping(value = "/fixedTimeSend", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiRestAction(error = "设置Kafka定时发送消息失败")
    public String fixedTimeSend() throws Exception {
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        FixedTimeSendModel fixedTimeSendModel = ApplicationHandler.instantiateObject(FixedTimeSendModel.class, requestParameters);
        fixedTimeSendModel.validateAndThrow();

        String topic = fixedTimeSendModel.getTopic();
        String key = fixedTimeSendModel.getKey();
        String data = fixedTimeSendModel.getData();
        Date sendTime = fixedTimeSendModel.getSendTime();

        Scheduler scheduler = schedulerFactoryBean.getScheduler();

        JobBuilder jobBuilder = JobBuilder.newJob(KafkaJob.class);
        jobBuilder.withIdentity(topic + "_" + key, KAFKA_DELAY_SEND_JOB_GROUP);
        JobDetail kafkaJobDetail = jobBuilder.build();
        JobDataMap jobDataMap = kafkaJobDetail.getJobDataMap();
        jobDataMap.put("topic", topic);
        jobDataMap.put("key", key);
        jobDataMap.put("data", data);

        SimpleScheduleBuilder simpleScheduleBuilder = SimpleScheduleBuilder.simpleSchedule();

        TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder.newTrigger();
        triggerBuilder.withIdentity(topic + "_" + key, KAFKA_DELAY_SEND_TRIGGER_GROUP);
        triggerBuilder.withSchedule(simpleScheduleBuilder);
        triggerBuilder.startAt(sendTime);

        Trigger trigger = triggerBuilder.build();
        scheduler.scheduleJob(kafkaJobDetail, trigger);
        return GsonUtils.toJson(ApiRest.builder().message("设置Kafka定时发送消息成功！").successful(true).build());
    }

    @RequestMapping(value = "/cancelFixedTimeSend", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiRestAction(error = "取消Kafka定时发送失败")
    public String cancelFixedTimeSend() throws Exception {
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        CancelFixedTimeSendModel cancelFixedTimeSendModel = ApplicationHandler.instantiateObject(CancelFixedTimeSendModel.class, requestParameters);
        cancelFixedTimeSendModel.validateAndThrow();

        String topic = cancelFixedTimeSendModel.getTopic();
        String key = cancelFixedTimeSendModel.getKey();

        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        TriggerKey triggerKey = TriggerKey.triggerKey(topic + "_" + key, KAFKA_DELAY_SEND_TRIGGER_GROUP);

        scheduler.pauseTrigger(triggerKey);
        scheduler.unscheduleJob(triggerKey);

        JobKey jobKey = JobKey.jobKey(topic + "_" + key, KAFKA_DELAY_SEND_JOB_GROUP);
        scheduler.deleteJob(jobKey);

        return GsonUtils.toJson(ApiRest.builder().message("取消Kafka定时发送成功！").successful(true).build());
    }
}
