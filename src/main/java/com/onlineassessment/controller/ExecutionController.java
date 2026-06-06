package com.onlineassessment.controller;

import com.onlineassessment.dto.JudgeResponseDto;
import com.onlineassessment.dto.RunCodeRequestDto;
import com.onlineassessment.entity.Question;
import com.onlineassessment.execution.CodeExecutionService;
import com.onlineassessment.execution.dto.CodeExecutionRequestDto;
import com.onlineassessment.execution.dto.ExecutionResultDto;
import com.onlineassessment.judge.JudgeResult;
import com.onlineassessment.judge.JudgeService;
import com.onlineassessment.judge.enums.JudgeMode;
import com.onlineassessment.service.QuestionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/execute")
public class ExecutionController {

    private final JudgeService judgeService;

    private final QuestionService questionService;

    private final CodeExecutionService
            codeExecutionService;

    public ExecutionController(
            JudgeService judgeService,
            QuestionService questionService,
            CodeExecutionService codeExecutionService
    ) {

        this.judgeService = judgeService;

        this.questionService = questionService;

        this.codeExecutionService =
                codeExecutionService;
    }

    /*
        RAW execution endpoint
        Used for:
        compile + execute testing
    */

    @PostMapping
    public ResponseEntity<ExecutionResultDto>
    executeCode(
            @Valid
            @RequestBody
            CodeExecutionRequestDto requestDto
    ) {

        ExecutionResultDto result =
                codeExecutionService
                        .executeJavaCode(
                                requestDto.getCode(),
                                requestDto.getInput()
                        );

        return ResponseEntity.ok(result);
    }

    /*
        Practice run endpoint

        Uses ONLY visible test cases
        Hidden test cases skipped
    */

    @PostMapping("/run")
    public ResponseEntity<JudgeResponseDto>
    runCode(
            @Valid
            @RequestBody
            RunCodeRequestDto requestDto
    ) {

        Question question =
                questionService
                        .getQuestionEntityById(
                                requestDto.getQuestionId()
                        );

        JudgeResult judgeResult =
                judgeService.judgeCode(
                        requestDto.getCode(),
                        question,
                        JudgeMode.RUN
                );

        JudgeResponseDto response =
                mapToDto(judgeResult);

        return ResponseEntity.ok(response);
    }


    private JudgeResponseDto mapToDto(
            JudgeResult judgeResult
    ) {

        JudgeResponseDto response =
                new JudgeResponseDto();

        response.setVerdict(
                judgeResult.getVerdict()
        );

        response.setPassedTestCases(
                judgeResult.getPassedTestCases()
        );

        response.setTotalTestCases(
                judgeResult.getTotalTestCases()
        );

        response.setMessage(
                judgeResult.getMessage()
        );

        return response;
    }
}