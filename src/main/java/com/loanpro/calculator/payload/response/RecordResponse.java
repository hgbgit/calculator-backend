package com.loanpro.calculator.payload.response;

import com.loanpro.calculator.models.Operation;
import com.loanpro.calculator.models.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RecordResponse {

    private Integer id;

    private Operation operation;

    private BigDecimal amount;

    private BigDecimal userBalance;

    private String operationResponse;

    private LocalDateTime date;
}