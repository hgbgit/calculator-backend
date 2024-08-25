package com.loanpro.calculator.tests.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.wnameless.json.unflattener.JsonUnflattener;
import com.loanpro.calculator.models.Operation;
import com.loanpro.calculator.models.Role;
import io.cucumber.java.DataTableType;

import java.util.Map;

public class TableTransformer {

    private final ObjectMapper objectMapper;

    public TableTransformer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @DataTableType
    public Role defineRole(Map<String, String> entry) throws JsonProcessingException {
        return objectMapper.readValue(this.getUnflatten(entry), Role.class);
    }

    @DataTableType
    public Operation defineOperation(Map<String, String> entry) throws JsonProcessingException {
        return objectMapper.readValue(this.getUnflatten(entry), Operation.class);
    }

    private String getUnflatten(Map<String, String> entry) throws JsonProcessingException {
        return JsonUnflattener.unflatten(objectMapper.writeValueAsString(entry));
    }
}
