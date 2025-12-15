package com.capstone.apistry.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExecuteResponse {
    private int statusCode;
    private String body;
    private Map<String, String> headers;
    private long responseTime;
}


