package com.loanpro.calculator.tests;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources/features",
        plugin = "pretty",
        glue = "com.loanpro")
public class LoanProCalculationApplicationTest {
}