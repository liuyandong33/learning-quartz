package build.dream.learning.jobs;

import org.quartz.*;

import java.util.concurrent.ConcurrentHashMap;

public class CustomJob implements Job {
    public static final ConcurrentHashMap<String, Job> CONCURRENT_HASH_MAP = new ConcurrentHashMap<String, Job>();

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDetail jobDetail = context.getJobDetail();
        JobKey jobKey = jobDetail.getKey();
        String name = jobKey.getName();
        String group = jobKey.getGroup();
        Job job = CONCURRENT_HASH_MAP.get(group + "_" + name);
        job.execute(context);
    }
}
