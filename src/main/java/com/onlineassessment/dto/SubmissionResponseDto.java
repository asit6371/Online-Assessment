package com.onlineassessment.dto;

import com.onlineassessment.judge.enums.Verdict;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SubmissionResponseDto {

    private long id;

    private long userId;

    private long sessionId;

    private long questionId;

    private String code;

    private Verdict verdict;

    private int passedTestCases;

    private int totalTestCases;

    private LocalDateTime submittedAt;
}