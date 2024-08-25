package com.loanpro.calculator.payload.request;

import com.loanpro.calculator.common.EOperation;
import jakarta.validation.constraints.Digits;

import java.math.BigDecimal;


public record CalculationRequest(@Digits(integer = 10, fraction = 2) BigDecimal a,
                                 @Digits(integer = 10, fraction = 2) BigDecimal b,
                                 EOperation operation) {
}
