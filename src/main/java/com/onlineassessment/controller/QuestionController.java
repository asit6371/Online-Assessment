package com.onlineassessment.controller;

import com.onlineassessment.dto.QuestionRequestDto;
import com.onlineassessment.dto.QuestionResponseDto;
import com.onlineassessment.service.QuestionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/questions")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    /**
     * FIX 1:
     *      when user don't have permission to create question.
     *      it should return message called don't have permission.
     *
     *
     */

    @PostMapping
    public ResponseEntity<QuestionResponseDto> createQuestion(
            @Valid @RequestBody QuestionRequestDto questionRequestDto) {

        QuestionResponseDto questionCreated =
                questionService.createQuestion(questionRequestDto);

        return ResponseEntity.ok(questionCreated);
    }

    @GetMapping("/getAllQuestions")
    public ResponseEntity<List<QuestionResponseDto>> getAllQuestion() {

        List<QuestionResponseDto> allQuestion =
                questionService.getAllQuestion();

        return ResponseEntity.ok(allQuestion);
    }

    @GetMapping("/{id}")
    public ResponseEntity<QuestionResponseDto> getQuestionById(
            @PathVariable long id) {

        QuestionResponseDto question =
                questionService.getQuestionById(id);

        return ResponseEntity.ok(question);
    }
}