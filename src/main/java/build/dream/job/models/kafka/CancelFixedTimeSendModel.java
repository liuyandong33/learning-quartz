package build.dream.job.models.kafka;

import build.dream.common.models.BasicModel;

import javax.validation.constraints.NotNull;

public class CancelFixedTimeSendModel extends BasicModel {
    @NotNull
    private String jobName;
    @NotNull
    private String jobGroup;
    @NotNull
    private String triggerName;
    @NotNull
    private String triggerGroup;

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobGroup() {
        return jobGroup;
    }

    public void setJobGroup(String jobGroup) {
        this.jobGroup = jobGroup;
    }

    public String getTriggerName() {
        return triggerName;
    }

    public void setTriggerName(String triggerName) {
        this.triggerName = triggerName;
    }

    public String getTriggerGroup() {
        return triggerGroup;
    }

    public void setTriggerGroup(String triggerGroup) {
        this.triggerGroup = triggerGroup;
    }
}
