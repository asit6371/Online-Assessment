package com.onlineassessment.dto;

import com.onlineassessment.enums.Difficulty;
import com.onlineassessment.enums.Topic;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class QuestionRequestDto {

    @NotBlank(message = "Title cannot be empty")
    private String title;

    @NotBlank(message = "Description cannot be empty")
    private String description;

    @NotBlank(message = "Sample input required")
    private String sampleInput;

    @NotBlank(message = "Sample output required")
    private String sampleOutput;

    @NotBlank(message = "Constraints required")
    private String constraints;

    @NotNull(message = "Difficulty required")
    private Difficulty difficulty;

    @NotNull(message = "Topic required")
    private Topic topic;
}