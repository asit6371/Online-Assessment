package com.onlineassessment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class RunCodeRequestDto {

    @Positive
    private long questionId;

    @NotBlank
    private String code;
}