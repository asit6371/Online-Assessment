package com.onlineassessment.service;

import com.onlineassessment.dto.SubmissionDto;
import com.onlineassessment.dto.SubmissionResponseDto;
import com.onlineassessment.entity.Question;
import com.onlineassessment.entity.Submission;
import com.onlineassessment.entity.Test;
import com.onlineassessment.entity.User;
import com.onlineassessment.enums.Status;
import com.onlineassessment.repository.QuestionRepository;
import com.onlineassessment.repository.SubmissionRepository;
import com.onlineassessment.repository.TestRepository;
import com.onlineassessment.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;


@Service
public class SubmissionService {


    @Autowired
    SubmissionRepository submissionRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    TestRepository testRepository;


    public SubmissionResponseDto createSubmission(SubmissionDto submissionDto) {
        Optional<User> existingUser = userRepository.findById(submissionDto.getUserId());

        if (existingUser.isEmpty()) {
            throw new RuntimeException("User does not exist!");
        }

        Optional<Test> existingTest = testRepository.findById(submissionDto.getTestId());

        if (existingTest.isEmpty()) {
            throw new RuntimeException("Test does not exist!");
        }

        Optional<Question> existingQuestion = questionRepository.findById(submissionDto.getQuestionId());

        if (existingQuestion.isEmpty()) {
            throw new RuntimeException("Question does not exist!");
        }

        Test test = existingTest.get();

        if (test.getStartTime() == null) {
            throw new RuntimeException("Test has not started!");
        }

        LocalDateTime currTime = LocalDateTime.now();

        if (currTime.isAfter(test.getEndTime())) {
            throw new RuntimeException("Test expired!");
        }

        Question question = existingQuestion.get();

        if(!test.getQuestions().contains(question)) {
            throw new RuntimeException("Invalid question!");
        }

        if (submissionDto.getCode() == null || submissionDto.getCode().isBlank()) {
            throw new RuntimeException("Invalid Code!");
        }

        User user = existingUser.get();

        Submission submission = new Submission();
        submission.setUser(user);
        submission.setTest(test);
        submission.setQuestion(question);
        submission.setCode(submissionDto.getCode());
        submission.setSubmittedAt(currTime);
        submission.setStatus(Status.PENDING);

        Submission savedSubmission =
                submissionRepository.save(submission);

        return mapToDto(savedSubmission);
    }


    private SubmissionResponseDto mapToDto(Submission submission) {

        SubmissionResponseDto response =
                new SubmissionResponseDto();

        response.setId(submission.getId());

        response.setUserId(submission.getUser().getId());

        response.setTestId(submission.getTest().getId());

        response.setQuestionId(submission.getQuestion().getId());

        response.setCode(submission.getCode());

        response.setStatus(submission.getStatus());

        response.setSubmittedAt(submission.getSubmittedAt());

        return response;
    }


}
