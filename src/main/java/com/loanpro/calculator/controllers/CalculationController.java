package com.loanpro.calculator.controllers;

import com.loanpro.calculator.payload.request.CalculationRequest;
import com.loanpro.calculator.payload.response.CalculationResponse;
import com.loanpro.calculator.services.OperationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/calculation")
@RequiredArgsConstructor
public class CalculationController {

    private final OperationService operationService;

    private static final Logger logger = LoggerFactory.getLogger(CalculationController.class);

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('USER')")
    public CalculationResponse calculate(@Valid @RequestBody CalculationRequest calculationRequest) {
        logger.info("Calculation request of operation: {}", calculationRequest.operation());
        String result = operationService.calculate(calculationRequest);
        return new CalculationResponse(result);
    }

}
