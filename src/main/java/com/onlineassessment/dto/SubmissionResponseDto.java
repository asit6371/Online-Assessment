package com.onlineassessment.dto;

import com.onlineassessment.enums.Status;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SubmissionResponseDto {

    private long id;

    private long userId;

    private long testId;

    private long questionId;

    private String code;

    private Status status;

    private LocalDateTime submittedAt;
}