package build.dream.job.models.kafka;

import build.dream.common.models.BasicModel;

import javax.validation.constraints.NotNull;

public class CancelFixedTimeSendModel extends BasicModel {
    @NotNull
    private String topic;

    @NotNull
    private String key;


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
}
