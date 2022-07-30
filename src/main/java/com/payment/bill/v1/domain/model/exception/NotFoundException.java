package com.payment.bill.v1.domain.model.exception;

public class NotFoundException extends RuntimeException{

    public NotFoundException(String msg) {
        super(msg);
    }
}
