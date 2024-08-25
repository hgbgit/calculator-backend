package com.loanpro.calculator.services.strategy;

import com.loanpro.calculator.payload.request.CalculationRequest;
import com.loanpro.calculator.common.EOperation;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DivisionOperation implements OperationStrategy<BigDecimal> {
    @Override
    public Boolean canHandle(CalculationRequest calculationRequest) {
        return EOperation.DIVISION.equals(calculationRequest.operation());
    }

    @Override
    public BigDecimal handle(CalculationRequest calculationRequest) {
        return calculationRequest.a().divide(calculationRequest.b());
    }
}
