package build.dream.job.controllers;

import build.dream.common.annotations.ApiRestAction;
import build.dream.common.api.ApiRest;
import build.dream.common.constants.Constants;
import build.dream.common.utils.ApplicationHandler;
import build.dream.common.utils.ConfigurationUtils;
import build.dream.common.utils.GsonUtils;
import build.dream.job.jobs.OrderInvalidJob;
import build.dream.job.models.order.StartJobModel;
import build.dream.job.models.order.StopJobModel;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigInteger;
import java.util.Date;
import java.util.Map;

@Controller
@RequestMapping(value = "/order")
public class OrderController {
    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;
    private static final String PARTITION_CODE = ConfigurationUtils.getConfigurationSafe(Constants.PARTITION_CODE);
    private static final String ORDER_JOB_GROUP = PARTITION_CODE + "_order";
    private static final String ORDER_TRIGGER_GROUP = PARTITION_CODE + "_order";

    @RequestMapping(value = "/startJob", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiRestAction(error = "开始失效订单定时任失败")
    public String startSimpleJob() throws Exception {
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        StartJobModel startJobModel = ApplicationHandler.instantiateObject(StartJobModel.class, requestParameters);
        startJobModel.validateAndThrow();

        String orderId = startJobModel.getOrderId().toString();
        BigInteger tenantId = startJobModel.getTenantId();
        BigInteger branchId = startJobModel.getBranchId();
        Date startTime = startJobModel.getStartTime();

        Scheduler scheduler = schedulerFactoryBean.getScheduler();

        JobBuilder jobBuilder = JobBuilder.newJob(OrderInvalidJob.class);
        jobBuilder.withIdentity(tenantId + "_" + branchId + "_" + orderId, ORDER_JOB_GROUP);
        JobDetail orderInvalidJobDetail = jobBuilder.build();
        JobDataMap jobDataMap = orderInvalidJobDetail.getJobDataMap();
        jobDataMap.put("tenantId", tenantId);
        jobDataMap.put("branchId", branchId);
        jobDataMap.put("orderId", orderId);

        SimpleScheduleBuilder simpleScheduleBuilder = SimpleScheduleBuilder.simpleSchedule();

        TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder.newTrigger();
        triggerBuilder.withIdentity(tenantId + "_" + branchId + "_" + orderId, ORDER_TRIGGER_GROUP);
        triggerBuilder.withSchedule(simpleScheduleBuilder);
        triggerBuilder.startAt(startTime);

        Trigger trigger = triggerBuilder.build();
        scheduler.scheduleJob(orderInvalidJobDetail, trigger);
        return GsonUtils.toJson(ApiRest.builder().message("开始失效订单定时任务成功！").successful(true).build());
    }

    @RequestMapping(value = "/stopJob", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String stopJob() throws Exception {
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        StopJobModel stopJobModel = ApplicationHandler.instantiateObject(StopJobModel.class, requestParameters);
        stopJobModel.validateAndThrow();

        String orderId = stopJobModel.getOrderId().toString();

        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        TriggerKey triggerKey = TriggerKey.triggerKey(orderId, ORDER_JOB_GROUP);

        scheduler.pauseTrigger(triggerKey);
        scheduler.unscheduleJob(triggerKey);

        JobKey jobKey = JobKey.jobKey(orderId, ORDER_TRIGGER_GROUP);
        scheduler.deleteJob(jobKey);

        return Constants.SUCCESS;
    }
}
