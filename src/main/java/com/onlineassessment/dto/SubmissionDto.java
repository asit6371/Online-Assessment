package com.onlineassessment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NonNull;

@Data
public class SubmissionDto {

    @Positive
    private long testId;

    @Positive
    private long questionId;

    @Positive
    private long userId;

    @NotBlank
    private String code;


}
