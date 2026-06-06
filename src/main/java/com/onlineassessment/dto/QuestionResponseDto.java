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

    /*
        Sent to frontend — user sees and edits this in the editor.
        driverCode is intentionally NOT included here.
        The driver stays hidden on the backend only.
    */
    private String starterCode;
}