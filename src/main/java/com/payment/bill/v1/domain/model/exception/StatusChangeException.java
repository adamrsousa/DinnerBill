package com.payment.bill.v1.domain.model.exception;

public class StatusChangeException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public StatusChangeException(String exception) {
        super(exception);
    }

}
