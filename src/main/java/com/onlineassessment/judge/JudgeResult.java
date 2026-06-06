package com.onlineassessment.judge;

import com.onlineassessment.judge.enums.Verdict;
import lombok.Data;

@Data
public class JudgeResult {

    private Verdict verdict;

    private int passedTestCases;

    private int totalTestCases;

    private String message;
}