package com.onlineassessment.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.util.List;

@Data
public class TestDto {

    @Positive
    private long userId;

    @NotNull
    @NotEmpty
    private List<Long> questionIds;
}
