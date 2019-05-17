package build.dream.job.controllers;

import build.dream.common.annotations.ApiRestAction;
import build.dream.common.api.ApiRest;
import build.dream.common.constants.Constants;
import build.dream.common.models.job.ScheduleSimpleJobModel;
import build.dream.common.utils.ApplicationHandler;
import build.dream.common.utils.ConfigurationUtils;
import build.dream.common.utils.GsonUtils;
import build.dream.common.utils.JobUtils;
import build.dream.job.jobs.KafkaFixedTimeSendJob;
import build.dream.job.models.kafka.CancelFixedTimeSendModel;
import build.dream.job.models.kafka.FixedTimeSendModel;
import org.apache.commons.lang.RandomStringUtils;
import org.quartz.JobDataMap;
import org.quartz.JobKey;
import org.quartz.TriggerKey;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "/kafka")
public class KafkaController {
    private static final String PARTITION_CODE = ConfigurationUtils.getConfiguration(Constants.PARTITION_CODE);
    private static final String KAFKA_FIXED_TIME_SEND_JOB_NAME_PREFIX = PARTITION_CODE + "_kafka_fixed_time_send_";
    private static final String KAFKA_FIXED_TIME_SEND_JOB_GROUP = PARTITION_CODE + "_kafka_fixed_time_send";
    private static final String KAFKA_FIXED_TIME_SEND_TRIGGER_NAME_PREFIX = PARTITION_CODE + "_kafka_fixed_time_send_";
    private static final String KAFKA_FIXED_TIME_SEND_TRIGGER_GROUP = PARTITION_CODE + "_kafka_fixed_time_send";

    @RequestMapping(value = "/fixedTimeSend", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiRestAction(error = "设置Kafka定时发送消息失败")
    public String fixedTimeSend() throws Exception {
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        FixedTimeSendModel fixedTimeSendModel = ApplicationHandler.instantiateObject(FixedTimeSendModel.class, requestParameters);
        fixedTimeSendModel.validateAndThrow();

        String topic = fixedTimeSendModel.getTopic();
        String data = fixedTimeSendModel.getData();
        Date sendTime = fixedTimeSendModel.getSendTime();

        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("topic", topic);
        jobDataMap.put("data", data);

        String jobId = RandomStringUtils.randomAlphanumeric(50);
        String triggerId = RandomStringUtils.randomAlphanumeric(50);
        ScheduleSimpleJobModel scheduleSimpleJobModel = ScheduleSimpleJobModel.builder()
                .jobName(KAFKA_FIXED_TIME_SEND_JOB_NAME_PREFIX + jobId)
                .jobGroup(KAFKA_FIXED_TIME_SEND_JOB_GROUP)
                .jobClass(KafkaFixedTimeSendJob.class)
                .jobDescription("Kafka定时发送")
                .jobDataMap(jobDataMap)
                .triggerName(KAFKA_FIXED_TIME_SEND_TRIGGER_NAME_PREFIX + triggerId)
                .triggerGroup(KAFKA_FIXED_TIME_SEND_TRIGGER_GROUP)
                .triggerDescription("Kafka定时发送")
                .startTime(sendTime)
                .build();
        JobUtils.scheduleSimpleJob(scheduleSimpleJobModel);

        Map<String, Object> result = new HashMap<String, Object>();
        result.put("jobId", jobId);
        result.put("triggerId", triggerId);
        return GsonUtils.toJson(ApiRest.builder().data(result).message("设置Kafka定时发送消息成功！").successful(true).build());
    }

    @RequestMapping(value = "/cancelFixedTimeSend", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiRestAction(error = "取消Kafka定时发送失败")
    public String cancelFixedTimeSend() throws Exception {
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        CancelFixedTimeSendModel cancelFixedTimeSendModel = ApplicationHandler.instantiateObject(CancelFixedTimeSendModel.class, requestParameters);
        cancelFixedTimeSendModel.validateAndThrow();

        String jobId = cancelFixedTimeSendModel.getJobId();
        String triggerId = cancelFixedTimeSendModel.getTriggerId();

        TriggerKey triggerKey = TriggerKey.triggerKey(KAFKA_FIXED_TIME_SEND_TRIGGER_NAME_PREFIX + triggerId, KAFKA_FIXED_TIME_SEND_TRIGGER_GROUP);
        if (JobUtils.checkExists(triggerKey)) {
            JobUtils.pauseTrigger(triggerKey);
            JobUtils.unscheduleJob(triggerKey);
        }

        JobKey jobKey = JobKey.jobKey(KAFKA_FIXED_TIME_SEND_JOB_NAME_PREFIX + jobId, KAFKA_FIXED_TIME_SEND_JOB_GROUP);
        if (JobUtils.checkExists(jobKey)) {
            JobUtils.deleteJob(jobKey);
        }

        return GsonUtils.toJson(ApiRest.builder().message("取消Kafka定时发送成功！").successful(true).build());
    }
}
