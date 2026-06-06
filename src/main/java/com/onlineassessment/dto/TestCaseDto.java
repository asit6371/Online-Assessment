package com.onlineassessment.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TestCaseDto {

    private String input;

    @NotBlank
    private String expectedOutput;

    private boolean hidden;
}