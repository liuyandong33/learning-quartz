package build.dream.job.jobs;

import build.dream.common.utils.ConfigurationUtils;
import build.dream.common.utils.LogUtils;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

import java.io.IOException;
import java.net.InetAddress;

@DisallowConcurrentExecution
public class TestJob implements Job {
    @Override
    public void execute(JobExecutionContext context) {
//        ThreadUtils.sleepSafe(5000);
        try {
            String ip = InetAddress.getLocalHost().getHostAddress();
            String port = ConfigurationUtils.getConfiguration("server.port");
            LogUtils.info(ip + ":" + port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
