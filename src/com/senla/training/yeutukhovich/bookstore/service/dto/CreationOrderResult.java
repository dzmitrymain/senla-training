package com.senla.training.yeutukhovich.bookstore.service.dto;

public class CreationOrderResult {

    private Long orderId;
    private Long requestId;

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }
}
