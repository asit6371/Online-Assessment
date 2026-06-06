package com.onlineassessment.service;

import com.onlineassessment.dto.AssessmentSessionResponseDto;
import com.onlineassessment.entity.AssessmentSession;
import com.onlineassessment.entity.Question;
import com.onlineassessment.entity.User;
import com.onlineassessment.enums.AssessmentCategory;
import com.onlineassessment.enums.SessionStatus;
import com.onlineassessment.exception.QuestionNotFoundException;
import com.onlineassessment.exception.UserNotFoundException;
import com.onlineassessment.repository.AssessmentSessionRepository;
import com.onlineassessment.repository.QuestionRepository;
import com.onlineassessment.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AssessmentSessionService {

    private static final int QUESTIONS_PER_SESSION = 2;
    private static final int DURATION_MINUTES = 60;

    private final AssessmentSessionRepository sessionRepository;
    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;

    public AssessmentSessionService(
            AssessmentSessionRepository sessionRepository,
            QuestionRepository questionRepository,
            UserRepository userRepository
    ) {
        this.sessionRepository = sessionRepository;
        this.questionRepository = questionRepository;
        this.userRepository = userRepository;
    }

    public AssessmentSessionResponseDto startSession(
            long userId,
            AssessmentCategory category
    ) {
        // 1. Validate user exists
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(
                        "User not found!"
                ));

        // 2. Fetch all questions from DB
        List<Question> allQuestions = questionRepository.findAll();

        if (allQuestions.size() < QUESTIONS_PER_SESSION) {
            throw new QuestionNotFoundException(
                    "Not enough questions in the database to start a session. " +
                            "Minimum required: " + QUESTIONS_PER_SESSION
            );
        }

        // 3. Shuffle and pick QUESTIONS_PER_SESSION random questions
        //    Collections.shuffle gives true randomness — no bias
        Collections.shuffle(allQuestions);

        List<Long> pickedQuestionIds = allQuestions
                .stream()
                .limit(QUESTIONS_PER_SESSION)
                .map(Question::getId)
                .collect(Collectors.toList());

        // 4. Create a fresh session for this user
        //    Every user gets their own independent session — no shared state
        LocalDateTime now = LocalDateTime.now(java.time.ZoneOffset.UTC);

        AssessmentSession session = new AssessmentSession();
        session.setUser(user);
        session.setCategory(category);
        session.setQuestionIds(pickedQuestionIds);
        session.setStartTime(now);
        session.setEndTime(now.plusMinutes(DURATION_MINUTES));
        session.setDurationMinutes(DURATION_MINUTES);
        session.setStatus(SessionStatus.IN_PROGRESS);

        AssessmentSession saved = sessionRepository.save(session);

        return mapToDto(saved);
    }

    public AssessmentSessionResponseDto getSession(long sessionId) {

        AssessmentSession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException(
                        "Session not found!"
                ));

        // Auto-expire if time has passed
        if (session.getStatus() == SessionStatus.IN_PROGRESS
                && LocalDateTime.now().isAfter(session.getEndTime())) {

            session.setStatus(SessionStatus.EXPIRED);
            sessionRepository.save(session);
        }

        return mapToDto(session);
    }

    private AssessmentSessionResponseDto mapToDto(AssessmentSession session) {

        AssessmentSessionResponseDto dto = new AssessmentSessionResponseDto();

        dto.setId(session.getId());
        dto.setUserId(session.getUser().getId());
        dto.setCategory(session.getCategory());
        dto.setQuestionIds(session.getQuestionIds());
        dto.setStartTime(session.getStartTime());
        dto.setEndTime(session.getEndTime());
        dto.setDurationMinutes(session.getDurationMinutes());
        dto.setStatus(session.getStatus());

        return dto;
    }
}