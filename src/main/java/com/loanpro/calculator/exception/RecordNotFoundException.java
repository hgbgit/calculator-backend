package com.loanpro.calculator.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class RecordNotFoundException extends RuntimeException {

    private final Long recordId;

    public RecordNotFoundException(Long recordId) {
        super(String.format("Record %d was not found.", recordId));
        this.recordId = recordId;
    }
}
