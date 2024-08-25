package com.loanpro.calculator.services.strategy;

import com.loanpro.calculator.payload.request.CalculationRequest;

public interface OperationStrategy<T> {

    public Boolean canHandle(CalculationRequest calculationRequest);

    public T handle(CalculationRequest calculationRequest);

}
