package build.dream.job.models.order;

import build.dream.common.models.BasicModel;

import java.math.BigInteger;

public class StopJobModel extends BasicModel {
    private BigInteger orderId;

    public BigInteger getOrderId() {
        return orderId;
    }

    public void setOrderId(BigInteger orderId) {
        this.orderId = orderId;
    }
}
