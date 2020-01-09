package build.dream.job.services;

import build.dream.common.api.ApiRest;
import build.dream.common.models.job.ScheduleCronJobModel;
import build.dream.common.models.job.ScheduleSimpleJobModel;
import build.dream.common.utils.DatabaseHelper;
import build.dream.common.utils.JobUtils;
import build.dream.common.utils.SearchModel;
import build.dream.job.domains.JobDetail;
import build.dream.job.domains.JobTrigger;
import build.dream.job.jobs.DeleteAllJobsModel;
import build.dream.job.models.job.RefreshJobsModel;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class JobService {
    @Transactional(rollbackFor = Exception.class)
    public ApiRest refreshJobs(RefreshJobsModel refreshJobsModel) throws SchedulerException, ClassNotFoundException {
        SearchModel searchModel = SearchModel.builder()
                .autoSetDeletedFalse()
                .build();
        List<JobDetail> jobDetails = DatabaseHelper.findAll(JobDetail.class, searchModel);
        List<JobTrigger> jobTriggers = DatabaseHelper.findAll(JobTrigger.class, searchModel);

        Map<BigInteger, JobTrigger> jobTriggerMap = jobTriggers.stream().collect(Collectors.toMap(jobTrigger -> jobTrigger.getId(), jobTrigger -> jobTrigger));

        for (JobDetail jobDetail : jobDetails) {
            JobTrigger jobTrigger = jobTriggerMap.get(jobDetail.getJobTriggerId());
            int type = jobTrigger.getType();
            if (type == 1) {
                ScheduleSimpleJobModel scheduleSimpleJobModel = ScheduleSimpleJobModel.builder()
                        .jobName(jobDetail.getJobName())
                        .jobGroup(jobDetail.getJobGroup())
                        .jobClass((Class<? extends Job>) Class.forName(jobDetail.getJobClassName()))
                        .jobDescription(jobDetail.getDescription())
                        .durability(jobDetail.isDurability())
                        .shouldRecover(jobDetail.isShouldRecover())
                        .triggerName(jobTrigger.getTriggerName())
                        .triggerGroup(jobTrigger.getTriggerGroup())
                        .interval(jobTrigger.getTimeInterval())
                        .repeatCount(jobTrigger.getRepeatCount())
                        .misfireInstruction(jobTrigger.getMisfireInstruction())
                        .triggerDescription(jobTrigger.getDescription())
                        .startTime(jobTrigger.getStartTime())
                        .endTime(jobTrigger.getEndTime())
                        .priority(jobTrigger.getPriority())
                        .calendarName(jobTrigger.getCalendarName())
                        .build();
                JobUtils.scheduleSimpleJob(scheduleSimpleJobModel);
            } else if (type == 2) {
                ScheduleCronJobModel scheduleCronJobModel = ScheduleCronJobModel.builder()
                        .jobName(jobDetail.getJobName())
                        .jobGroup(jobDetail.getJobGroup())
                        .jobClass((Class<? extends Job>) Class.forName(jobDetail.getJobClassName()))
                        .jobDescription(jobDetail.getDescription())
//                        .durability(jobDetail.isDurability())
//                        .shouldRecover(jobDetail.isShouldRecover())
                        .triggerName(jobTrigger.getTriggerName())
                        .triggerGroup(jobTrigger.getTriggerGroup())
                        .cronExpression(jobTrigger.getCronExpression())
//                        .misfireInstruction(jobTrigger.getMisfireInstruction())
//                        .triggerDescription(jobTrigger.getDescription())
//                        .startTime(jobTrigger.getStartTime())
//                        .endTime(jobTrigger.getEndTime())
//                        .priority(jobTrigger.getPriority())
//                        .calendarName(jobTrigger.getCalendarName())
                        .build();
                JobUtils.scheduleCronJob(scheduleCronJobModel);
            }
        }

        return ApiRest.builder().successful(true).build();
    }

    @Transactional(rollbackFor = Exception.class)
    public ApiRest deleteAllJobs(DeleteAllJobsModel deleteAllJobsModel) throws SchedulerException {
        Scheduler scheduler = JobUtils.obtainScheduler();
        List<String> triggerGroupNames = scheduler.getTriggerGroupNames();
        for (String triggerGroupName : triggerGroupNames) {
            GroupMatcher groupMatcher = GroupMatcher.groupEquals(triggerGroupName);
            Set<TriggerKey> triggerKeys = scheduler.getTriggerKeys(groupMatcher);
            for (TriggerKey triggerKey : triggerKeys) {
                JobUtils.unscheduleJob(triggerKey);
            }
        }

        List<String> jobGroupNames = scheduler.getJobGroupNames();
        for (String jobGroupName : jobGroupNames) {
            GroupMatcher groupMatcher = GroupMatcher.groupEquals(jobGroupName);
            Set<JobKey> jobKeys = scheduler.getJobKeys(groupMatcher);
            for (JobKey jobKey : jobKeys) {
                JobUtils.deleteJob(jobKey);
            }
        }
        return ApiRest.builder().successful(true).build();
    }
}
