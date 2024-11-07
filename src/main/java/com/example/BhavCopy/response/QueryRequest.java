package com.example.BhavCopy.response;

import java.util.Map;

public class QueryRequest {
    private String operation;
    private Map<String, Object> parameters;

    // Getters and Setters
    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, Object> parameters) {
        this.parameters = parameters;
    }
}
