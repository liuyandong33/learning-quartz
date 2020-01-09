package build.dream.job.domains;

import build.dream.common.annotations.Column;
import build.dream.common.basic.BasicDomain;

import java.math.BigInteger;

public class JobDetail extends BasicDomain {
    private String jobName;
    private String jobGroup;
    private String description;
    private String jobClassName;
    private boolean durability;
    private boolean shouldRecover;
    private BigInteger jobTriggerId;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getJobClassName() {
        return jobClassName;
    }

    public void setJobClassName(String jobClassName) {
        this.jobClassName = jobClassName;
    }

    public boolean isDurability() {
        return durability;
    }

    public void setDurability(boolean durability) {
        this.durability = durability;
    }

    public boolean isShouldRecover() {
        return shouldRecover;
    }

    public void setShouldRecover(boolean shouldRecover) {
        this.shouldRecover = shouldRecover;
    }

    public BigInteger getJobTriggerId() {
        return jobTriggerId;
    }

    public void setJobTriggerId(BigInteger jobTriggerId) {
        this.jobTriggerId = jobTriggerId;
    }
}
