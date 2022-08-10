package com.payment.bill.v1.api.controller.exception;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import lombok.Getter;

@RestControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(PaymentRequestException.class)
    public ResponseEntity<Object> handlePaymentRequestException(PaymentRequestException ex,
                                                                WebRequest request) {
        String userMessage = "Error at generating payment";
        String devMessage = ex.toString();
        List<StandardError> errors = Arrays.asList(new StandardError(userMessage, devMessage));
        return handleExceptionInternal(ex, errors, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(StatusChangeException.class)
    public ResponseEntity<Object> handleChangedStatusException(StatusChangeException ex,
                                                               WebRequest request) {
        String userMessage = "Error at verifying payment status";
        String devMessage = ex.toString();
        List<StandardError> errors = Arrays.asList(new StandardError(userMessage, devMessage));
        return handleExceptionInternal(ex, errors, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    public static class StandardError {

        @Getter
        private String userMessage;
        @Getter
        private String devMessage;

        public StandardError(String userMessage, String devMessage) {
            this.userMessage = userMessage;
            this.devMessage = devMessage;
        }
    }
}
