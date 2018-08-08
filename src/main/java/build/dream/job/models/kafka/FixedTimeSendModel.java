package build.dream.job.models.kafka;

import build.dream.common.models.BasicModel;

import javax.validation.constraints.NotNull;
import java.util.Date;

public class FixedTimeSendModel extends BasicModel {
    @NotNull
    private String topic;

    @NotNull
    private String key;

    @NotNull
    private String data;

    @NotNull
    private Date sendTime;

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }
}
