package com.onlineassessment.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.util.List;

@Data
public class TestRequestDto {

    @Positive
    private long userId;

    @NotNull
    @NotEmpty
    private List<Long> questionIds;

    private String title;

    private String description;

    private Integer durationMinutes;
}
