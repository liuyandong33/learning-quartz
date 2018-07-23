package build.dream.learning.models.order;

import build.dream.common.models.BasicModel;

import java.math.BigInteger;

public class StartJobModel extends BasicModel {
    private BigInteger orderId;
    private Long interval;

    public BigInteger getOrderId() {
        return orderId;
    }

    public void setOrderId(BigInteger orderId) {
        this.orderId = orderId;
    }

    public Long getInterval() {
        return interval;
    }

    public void setInterval(Long interval) {
        this.interval = interval;
    }
}
