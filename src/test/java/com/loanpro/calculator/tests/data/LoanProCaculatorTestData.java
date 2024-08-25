package com.loanpro.calculator.tests.data;

import com.loanpro.calculator.payload.request.CalculationRequest;
import com.loanpro.calculator.payload.request.SignupRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.test.web.servlet.ResultActions;

@Getter
@Setter
@ToString
public class LoanProCaculatorTestData {

    private ResultActions signUpResultActions;
    private ResultActions signInResultActions;
    private ResultActions operationResultActions;
    private String requestJson;
    private SignupRequest signupRequest;
    private CalculationRequest calculationRequest;
    private String authorizationToken;

    public LoanProCaculatorTestData() {
        this.reset();
    }

    public void reset() {
        this.signUpResultActions = null;
        this.signInResultActions = null;
        this.operationResultActions = null;
        this.requestJson = null;
        this.signupRequest = null;
        this.calculationRequest = null;
    }

}
