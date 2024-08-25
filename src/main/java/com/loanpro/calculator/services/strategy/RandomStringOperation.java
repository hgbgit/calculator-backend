package com.loanpro.calculator.services.strategy;

import com.loanpro.calculator.client.RandomStringClient;
import com.loanpro.calculator.common.EOperation;
import com.loanpro.calculator.payload.request.CalculationRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RandomStringOperation implements OperationStrategy<String> {
    private final RandomStringClient randomStringClient;
    private final String digits;
    private final String upperAlpha;
    private final String lowerAlpha;
    private final String unique;
    private final String rnd;
    private final String format;

    public RandomStringOperation(final RandomStringClient randomStringClient,
                                 @Value("${client.string.digits}") final String digits,
                                 @Value("${client.string.upperalpha}") final String upperAlpha,
                                 @Value("${client.string.loweralpha}") final String lowerAlpha,
                                 @Value("${client.string.unique}") final String unique,
                                 @Value("${client.string.rnd}") final String rnd,
                                 @Value("${client.string.format}") final String format) {

        this.randomStringClient = randomStringClient;
        this.digits = digits;
        this.upperAlpha = upperAlpha;
        this.lowerAlpha = lowerAlpha;
        this.unique = unique;
        this.rnd = rnd;
        this.format = format;
    }


    @Override
    public Boolean canHandle(CalculationRequest calculationRequest) {
        return EOperation.RANDOM_STRING.equals(calculationRequest.operation());
    }

    @Override
    public String handle(CalculationRequest calculationRequest) {
        return randomStringClient.getRandomString(calculationRequest.a().intValue(), calculationRequest.b().intValue(), digits, upperAlpha, lowerAlpha, unique, rnd, format).replace("\n", "");
    }
}
