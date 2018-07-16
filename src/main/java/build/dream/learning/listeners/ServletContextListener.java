package build.dream.learning.listeners;

import build.dream.common.listeners.BasicServletContextListener;
import build.dream.learning.jobs.JobScheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletContextEvent;
import javax.servlet.annotation.WebListener;

@WebListener
public class ServletContextListener extends BasicServletContextListener {
    @Autowired
    private JobScheduler jobScheduler;
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        super.contextInitialized(servletContextEvent);
        try {
            jobScheduler.scheduler();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
}
