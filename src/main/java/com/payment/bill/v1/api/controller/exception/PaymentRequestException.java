package com.payment.bill.v1.api.controller.exception;

public class PaymentRequestException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public PaymentRequestException(String exception) {
        super(exception);
    }
}
