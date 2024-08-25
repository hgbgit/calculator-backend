package com.loanpro.calculator.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.math.BigDecimal;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class InsufficientBalanceException extends RuntimeException {

    private final BigDecimal balance;

    private final BigDecimal cost;

    public InsufficientBalanceException(BigDecimal balance, BigDecimal cost) {
        super(String.format("Current balance: %.2f, Operation cost: %.2f", balance, cost));
        this.balance = balance;
        this.cost = cost;
    }
}
