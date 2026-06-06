package com.onlineassessment.dto;

import com.onlineassessment.judge.enums.Verdict;
import lombok.Data;

@Data
public class JudgeResponseDto {

    private Verdict verdict;

    private int passedTestCases;

    private int totalTestCases;

    private String message;
}