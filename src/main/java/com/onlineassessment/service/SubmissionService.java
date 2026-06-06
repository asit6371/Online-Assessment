package com.onlineassessment.service;

import com.onlineassessment.dto.SubmissionDto;
import com.onlineassessment.dto.SubmissionResponseDto;
import com.onlineassessment.entity.Question;
import com.onlineassessment.entity.Submission;
import com.onlineassessment.entity.User;
import com.onlineassessment.exception.QuestionNotFoundException;
import com.onlineassessment.exception.UserNotFoundException;
import com.onlineassessment.judge.JudgeResult;
import com.onlineassessment.judge.JudgeService;
import com.onlineassessment.judge.enums.JudgeMode;
import com.onlineassessment.repository.QuestionRepository;
import com.onlineassessment.repository.SubmissionRepository;
import com.onlineassessment.repository.UserRepository;
import org.springframework.stereotype.Service;
import com.onlineassessment.entity.AssessmentSession;
import com.onlineassessment.enums.SessionStatus;
import com.onlineassessment.repository.AssessmentSessionRepository;

import java.time.LocalDateTime;

@Service
public class SubmissionService {

    private final SubmissionRepository
            submissionRepository;

    private final UserRepository
            userRepository;

    private final QuestionRepository
            questionRepository;

    private final AssessmentSessionRepository
            sessionRepository;

    private final JudgeService
            judgeService;

    public SubmissionService(
            SubmissionRepository submissionRepository,
            UserRepository userRepository,
            QuestionRepository questionRepository,
            AssessmentSessionRepository sessionRepository,
            JudgeService judgeService
    ) {

        this.submissionRepository =
                submissionRepository;

        this.userRepository =
                userRepository;

        this.questionRepository =
                questionRepository;

        this.sessionRepository =
                sessionRepository;

        this.judgeService =
                judgeService;
    }

    public SubmissionResponseDto
    createSubmission(
            SubmissionDto submissionDto
    ) {

        User user =
                getUser(
                        submissionDto.getUserId()
                );

        AssessmentSession session =
                getSession(
                        submissionDto.getSessionId()
                );

        Question question =
                getQuestion(
                        submissionDto.getQuestionId()
                );

        validateSession(session);

        validateQuestionInsideSession(
                session,
                question
        );

        validateCode(
                submissionDto.getCode()
        );

        LocalDateTime currentTime =
                LocalDateTime.now();

        Submission submission =
                new Submission();

        submission.setUser(user);

        submission.setSession(session);

        submission.setQuestion(question);

        submission.setCode(
                submissionDto.getCode()
        );

        JudgeResult judgeResult =
                judgeService.judgeCode(
                        submissionDto.getCode(),
                        question,
                        JudgeMode.SUBMIT
                );

        submission.setVerdict(
                judgeResult.getVerdict()
        );

        submission.setPassedTestCases(
                judgeResult
                        .getPassedTestCases()
        );

        submission.setTotalTestCases(
                judgeResult
                        .getTotalTestCases()
        );

        submission.setSubmittedAt(
                currentTime
        );

        Submission savedSubmission =
                submissionRepository.save(
                        submission
                );

        return mapToDto(
                savedSubmission
        );
    }

    private User getUser(long userId) {

        return userRepository
                .findById(userId)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User does not exist!"
                        )
                );
    }

    private AssessmentSession getSession(
            long sessionId
    ) {

        return sessionRepository
                .findById(sessionId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Session does not exist!"
                        )
                );
    }

    private Question getQuestion(
            long questionId
    ) {

        return questionRepository
                .findById(questionId)
                .orElseThrow(() ->
                        new QuestionNotFoundException(
                                "Question does not exist!"
                        )
                );
    }

    private void validateSession(
            AssessmentSession session
    ) {

        if (session.getStatus() != SessionStatus.IN_PROGRESS) {

            throw new RuntimeException(
                    "Session is not active!"
            );
        }

        if (LocalDateTime.now()
                .isAfter(
                        session.getEndTime()
                )) {

            throw new RuntimeException(
                    "Session expired!"
            );
        }
    }

    private void validateQuestionInsideSession(
            AssessmentSession session,
            Question question
    ) {

        if (!session.getQuestionIds()
                .contains(question.getId())) {

            throw new QuestionNotFoundException(
                    "Question does not belong to this session!"
            );
        }
    }
    private void validateCode(
            String code
    ) {

        if (code == null ||
                code.isBlank()) {

            throw new RuntimeException(
                    "Invalid Code!"
            );
        }
    }

    private SubmissionResponseDto
    mapToDto(
            Submission submission
    ) {

        SubmissionResponseDto response =
                new SubmissionResponseDto();

        response.setId(
                submission.getId()
        );

        response.setUserId(
                submission.getUser()
                        .getId()
        );

        response.setSessionId(
                submission.getSession()
                        .getId()
        );

        response.setQuestionId(
                submission.getQuestion()
                        .getId()
        );

        response.setCode(
                submission.getCode()
        );

        response.setVerdict(
                submission.getVerdict()
        );

        response.setPassedTestCases(
                submission
                        .getPassedTestCases()
        );

        response.setTotalTestCases(
                submission
                        .getTotalTestCases()
        );

        response.setSubmittedAt(
                submission.getSubmittedAt()
        );

        return response;
    }
}