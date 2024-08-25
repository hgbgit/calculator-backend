package com.loanpro.calculator.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_IMPLEMENTED)
public class OperationNotSupportedException extends RuntimeException {

    private final String operation;

    public OperationNotSupportedException(String operation) {
        super(String.format("Operation %s is not supported.", operation));
        this.operation = operation;
    }
}
