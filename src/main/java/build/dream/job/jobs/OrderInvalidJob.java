package build.dream.job.jobs;

import build.dream.common.utils.ConfigurationUtils;
import build.dream.common.utils.GsonUtils;
import build.dream.job.constants.Constants;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class OrderInvalidJob implements Job {
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public void execute(JobExecutionContext context) {
        JobDetail jobDetail = context.getJobDetail();
        JobDataMap jobDataMap = jobDetail.getJobDataMap();

        Map<String, Object> info = new HashMap<String, Object>();
        info.put("tenantId", jobDataMap.get("tenantId"));
        info.put("branchId", jobDataMap.get("branchId"));
        info.put("orderId", jobDataMap.get("orderId"));

        String topic = ConfigurationUtils.getConfigurationSafe(Constants.ORDER_INVALID_MESSAGE_TOPIC);
        kafkaTemplate.send(topic, UUID.randomUUID().toString(), GsonUtils.toJson(info));
    }
}
