package com.onlineassessment.judge;

import com.onlineassessment.entity.Question;
import com.onlineassessment.entity.TestCase;
import com.onlineassessment.exception.InvalidCodeException;
import com.onlineassessment.execution.CodeExecutionService;
import com.onlineassessment.execution.dto.ExecutionResultDto;
import com.onlineassessment.judge.enums.JudgeMode;
import com.onlineassessment.judge.enums.Verdict;
import org.springframework.stereotype.Service;

@Service
public class JudgeService {

    private final CodeExecutionService codeExecutionService;

    public JudgeService(CodeExecutionService codeExecutionService) {
        this.codeExecutionService = codeExecutionService;
    }

    /*
        Judges user's Solution code against test cases.

        JudgeMode.RUN    → only visible test cases (practice run)
        JudgeMode.SUBMIT → all test cases including hidden ones

        The user's code is ONLY the Solution class.
        The driverCode (Main class) is stored on the question in DB.
        CodeExecutionService combines them before compiling.
    */
    public JudgeResult judgeCode(
            String userCode,
            Question question,
            JudgeMode mode
    ) {
        JudgeResult result = new JudgeResult();

        // Validate driver code exists for this question
        if (question.getDriverCode() == null ||
                question.getDriverCode().isBlank()) {
            throw new InvalidCodeException(
                    "This question is not properly configured. " +
                            "Driver code is missing."
            );
        }

        int passedCount = 0;
        int totalTestCases = 0;

        for (TestCase testCase : question.getTestCases()) {

            // RUN mode skips hidden test cases
            if (mode == JudgeMode.RUN && testCase.isHidden()) {
                continue;
            }

            totalTestCases++;

            // Execute: driver wraps user's Solution class
            ExecutionResultDto executionResult =
                    codeExecutionService.executeWithDriver(
                            question.getDriverCode(),
                            userCode,
                            testCase.getInput()
                    );

            if (!executionResult.isSuccess()) {
                String error = executionResult.getError();

                result.setPassedTestCases(passedCount);
                result.setTotalTestCases(totalTestCases);
                result.setMessage(error);

                if (error.equals("Time Limit Exceeded")) {
                    result.setVerdict(Verdict.TIME_LIMIT_EXCEEDED);
                } else if (error.contains("error:")) {
                    result.setVerdict(Verdict.COMPILATION_ERROR);
                } else {
                    result.setVerdict(Verdict.RUNTIME_ERROR);
                }

                return result;
            }

            String actualOutput = executionResult.getOutput().trim();
            String expectedOutput = testCase.getExpectedOutput().trim();

            if (!actualOutput.equals(expectedOutput)) {
                result.setVerdict(Verdict.WRONG_ANSWER);
                result.setMessage(
                        "Expected: " + expectedOutput +
                                " | Got: " + actualOutput
                );
                result.setPassedTestCases(passedCount);
                result.setTotalTestCases(totalTestCases);
                return result;
            }

            passedCount++;
        }

        result.setVerdict(Verdict.ACCEPTED);
        result.setMessage("All test cases passed");
        result.setPassedTestCases(passedCount);
        result.setTotalTestCases(totalTestCases);
        return result;
    }
}