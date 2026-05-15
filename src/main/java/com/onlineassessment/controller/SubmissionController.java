package com.onlineassessment.controller;

import com.onlineassessment.dto.SubmissionDto;
import com.onlineassessment.dto.SubmissionResponseDto;
import com.onlineassessment.entity.Submission;
import com.onlineassessment.service.SubmissionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/submissions")
public class SubmissionController {

    @Autowired
    SubmissionService submissionService;


    @PostMapping
    public ResponseEntity<SubmissionResponseDto> createSubmission(
            @Valid @RequestBody SubmissionDto submissionDto) {

        SubmissionResponseDto submission = submissionService.createSubmission(submissionDto);

        return ResponseEntity.ok(submission);
    }
}
