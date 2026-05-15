package com.onlineassessment.dto;

import com.onlineassessment.enums.Difficulty;
import com.onlineassessment.enums.Topic;
import lombok.Data;

@Data
public class QuestionResponseDto {

    private long id;

    private String title;

    private String description;

    private String sampleInput;

    private String sampleOutput;

    private String constraints;

    private Difficulty difficulty;

    private Topic topic;
}