package build.dream.job.jobs;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;

public class KafkaJob implements Job {
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public void execute(JobExecutionContext context) {
        JobDetail jobDetail = context.getJobDetail();
        JobDataMap jobDataMap = jobDetail.getJobDataMap();

        String topic = jobDataMap.getString("topic");
        String key = jobDataMap.getString("key");
        String data = jobDataMap.getString("data");
        ListenableFuture<SendResult<String, String>> listenableFuture = kafkaTemplate.send(topic, key, data);
        try {
            listenableFuture.get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
