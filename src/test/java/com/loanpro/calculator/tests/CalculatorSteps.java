package com.loanpro.calculator.tests;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.loanpro.calculator.LoanProCalculatorApplication;
import com.loanpro.calculator.client.RandomStringClient;
import com.loanpro.calculator.common.EOperation;
import com.loanpro.calculator.models.Operation;
import com.loanpro.calculator.models.Role;
import com.loanpro.calculator.payload.request.CalculationRequest;
import com.loanpro.calculator.payload.request.LoginRequest;
import com.loanpro.calculator.payload.request.SignupRequest;
import com.loanpro.calculator.payload.response.CalculationResponse;
import com.loanpro.calculator.payload.response.UserInfoResponse;
import com.loanpro.calculator.repository.OperationRepository;
import com.loanpro.calculator.repository.RoleRepository;
import com.loanpro.calculator.tests.data.LoanProCaculatorTestData;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import org.junit.Assert;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.reset;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ContextConfiguration(classes = {LoanProCalculatorApplication.class})
@AutoConfigureMockMvc
@ActiveProfiles("test")
@CucumberContextConfiguration
@MockBean(RandomStringClient.class)
public class CalculatorSteps {

    public static final String SIGNUP_ENDPOINT = "/api/auth/signup";
    public static final String SIGNIN_ENDPOINT = "/api/auth/signin";
    public static final String CALCULATION_ENDPOINT = "/api/calculation";
    private final RoleRepository roleRepository;
    private final OperationRepository operationRepository;
    private final RandomStringClient randomStringClient;
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    private LoanProCaculatorTestData testData;

    public CalculatorSteps(MockMvc mockMvc, ObjectMapper objectMapper, RoleRepository roleRepository, OperationRepository operationRepository, RandomStringClient randomStringClient) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.randomStringClient = randomStringClient;
        this.testData = new LoanProCaculatorTestData();
        this.roleRepository = roleRepository;
        this.operationRepository = operationRepository;
    }

    @Before
    public void setUp() {
        this.testData.reset();
    }

    @After
    public void wrapUp() {
        reset();
    }


    @Given("that the database contains the following roles:")
    public void thatTheDatabaseContainsTheFollowingRoles(List<Role> roles) {
        this.roleRepository.saveAll(roles);
    }

    @And("the database contains the following operations:")
    public void thatTheDatabaseContainsTheFollowingOperations(List<Operation> operations) {
        this.operationRepository.saveAll(operations);
    }

    @And("the random string server is up and will return the following string: {string}")
    public void theRandomStringServerIsUpAndWillReturnTheFollowingString(String randomString) {
        given(randomStringClient.getRandomString(anyInt(), anyInt(), anyString(), anyString(),
                anyString(), anyString(), anyString(), anyString())).willReturn(randomString);
    }

    @And("^the user wants to make a new signup with the following values:")
    public void theUserWantsToMakeANewSignUpWithTheFollowingValues(Map<String, String> values) throws Exception {
        this.testData.setSignupRequest(objectMapper.readValue(objectMapper.writeValueAsString(values), SignupRequest.class));

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders.post(SIGNUP_ENDPOINT)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(this.testData.getSignupRequest()));

        ResultActions resultActions = mockMvc.perform(mockHttpServletRequestBuilder);

        this.testData.setSignUpResultActions(resultActions);
    }

    @And("^this user signin into calculator")
    public void thisuserSignInIntoCalculator() throws Exception {
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders.post(SIGNIN_ENDPOINT)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(new LoginRequest(this.testData.getSignupRequest().getUsername(), this.testData.getSignupRequest().getPassword())));

        ResultActions resultActions = mockMvc.perform(mockHttpServletRequestBuilder);
        UserInfoResponse userInfoResponse = objectMapper.readValue(resultActions.andReturn().getResponse().getContentAsString(), UserInfoResponse.class);
        this.testData.setAuthorizationToken(userInfoResponse.getToken());
        this.testData.setSignInResultActions(resultActions);
    }

    @And("this user want to create a new operation {string} with a as {string} and b as {string}:")
    public void thisUserWantToCreateANewOperationWithAAsAndBAs(String operation, String a, String b) {
        CalculationRequest calculationRequest = new CalculationRequest(new BigDecimal(a), new BigDecimal(b), EOperation.valueOf(operation));
        this.testData.setCalculationRequest(calculationRequest);
    }

    @When("this user request the new operation")
    public void thisUserRequestTheNewOperation() throws Exception {
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders.post(CALCULATION_ENDPOINT)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", this.testData.getAuthorizationToken())
                .content(objectMapper.writeValueAsString(this.testData.getCalculationRequest()));

        ResultActions resultActions = mockMvc.perform(mockHttpServletRequestBuilder);
        this.testData.setOperationResultActions(resultActions);
    }

    @Then("the result of the operation will be: {string}")
    public void theResultOfTheOperationWillBe(String result) throws UnsupportedEncodingException, JsonProcessingException {
        CalculationResponse response = objectMapper.readValue(this.testData.getOperationResultActions().andReturn().getResponse().getContentAsString(), CalculationResponse.class);

        Assert.assertEquals(result, response.getResult());
    }

    @And("the calculator backend service will return status: {int}")
    public void theCalculatorBackendServiceWillReturnStatusHttpStatus(int expectedStatus) throws Exception {
        testData.getOperationResultActions().andExpect(status().is(expectedStatus));
    }

}
