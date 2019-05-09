package build.dream.job.jobs;

import org.apache.commons.collections.MapUtils;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.UUID;

public class SimpleJob implements Job {
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public void execute(JobExecutionContext context) {
        JobDetail jobDetail = context.getJobDetail();
        JobDataMap jobDataMap = jobDetail.getJobDataMap();
        String topic = MapUtils.getString(jobDataMap, "topic");
        String data = MapUtils.getString(jobDataMap, "data");
        kafkaTemplate.send(topic, UUID.randomUUID().toString(), data);
    }
}
