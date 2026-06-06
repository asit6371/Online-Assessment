package com.onlineassessment.execution.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CodeExecutionRequestDto {

    @NotBlank(message = "Code cannot be empty")
    private String code;

    private String input;
}