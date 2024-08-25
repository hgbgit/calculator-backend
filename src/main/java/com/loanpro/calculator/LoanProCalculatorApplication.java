package com.loanpro.calculator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EntityScan(basePackages = "com.loanpro.calculator.models")
@EnableJpaAuditing
@EnableSpringDataWebSupport
@EnableFeignClients
public class LoanProCalculatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(LoanProCalculatorApplication.class, args);
    }

}
