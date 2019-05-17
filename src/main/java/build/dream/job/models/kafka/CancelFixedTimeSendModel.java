package build.dream.job.models.kafka;

import build.dream.common.models.BasicModel;

import javax.validation.constraints.NotNull;

public class CancelFixedTimeSendModel extends BasicModel {
    @NotNull
    private String jobId;
    @NotNull
    private String triggerId;

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getTriggerId() {
        return triggerId;
    }

    public void setTriggerId(String triggerId) {
        this.triggerId = triggerId;
    }
}
