package com.onlineassessment.execution.dto;

import lombok.Data;

@Data
public class ExecutionResultDto {

    private boolean success;
    private String output;
    private String error;
}
