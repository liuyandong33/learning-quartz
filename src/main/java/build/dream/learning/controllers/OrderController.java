package build.dream.learning.controllers;

import build.dream.common.annotations.ApiRestAction;
import build.dream.common.api.ApiRest;
import build.dream.common.constants.Constants;
import build.dream.common.utils.ApplicationHandler;
import build.dream.common.utils.ConfigurationUtils;
import build.dream.common.utils.GsonUtils;
import build.dream.learning.jobs.CustomJob;
import build.dream.learning.models.order.StartJobModel;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigInteger;
import java.util.Map;

@Controller
@RequestMapping(value = "/order")
public class OrderController {
    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;
    private static final String PARTITION_CODE = ConfigurationUtils.getConfigurationSafe(Constants.PARTITION_CODE);

    @RequestMapping(value = "/startJob", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @ApiRestAction(error = "开始失效订单定时任失败")
    public String startSimpleJob() throws Exception {
        Map<String, String> requestParameters = ApplicationHandler.getRequestParameters();
        StartJobModel startJobModel = ApplicationHandler.instantiateObject(StartJobModel.class, requestParameters);
        startJobModel.validateAndThrow();

        BigInteger orderId = startJobModel.getOrderId();
        long interval = startJobModel.getInterval();

        Scheduler scheduler = schedulerFactoryBean.getScheduler();

        JobBuilder jobBuilder = JobBuilder.newJob(CustomJob.class);
        jobBuilder.withIdentity(orderId.toString(), PARTITION_CODE + "_order");
        JobDetail dataJobDetail = jobBuilder.build();

        SimpleScheduleBuilder simpleScheduleBuilder = SimpleScheduleBuilder.simpleSchedule();
        simpleScheduleBuilder.withRepeatCount(1);
        simpleScheduleBuilder.withIntervalInMilliseconds(interval);

        TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder.newTrigger();
        triggerBuilder.withIdentity("order", PARTITION_CODE + "_order");
        triggerBuilder.withSchedule(simpleScheduleBuilder);

        Trigger trigger = triggerBuilder.build();
        scheduler.scheduleJob(dataJobDetail, trigger);
        return GsonUtils.toJson(ApiRest.builder().message("开始失效订单定时任务成功！").successful(true).build());
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
