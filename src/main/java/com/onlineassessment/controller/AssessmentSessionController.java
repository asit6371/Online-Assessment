package com.onlineassessment.controller;

import com.onlineassessment.dto.AssessmentSessionResponseDto;
import com.onlineassessment.enums.AssessmentCategory;
import com.onlineassessment.service.AssessmentSessionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sessions")
public class AssessmentSessionController {

    private final AssessmentSessionService sessionService;

    public AssessmentSessionController(
            AssessmentSessionService sessionService
    ) {
        this.sessionService = sessionService;
    }

    /*
        POST /sessions/start/{category}?userId=1
        Creates a brand new session for this user with random questions.
        Every call = new session = new random questions.
        No user is ever blocked from starting.
    */
    @PostMapping("/start/{category}")
    public ResponseEntity<AssessmentSessionResponseDto> startSession(
            @PathVariable AssessmentCategory category,
            @RequestParam long userId
    ) {
        AssessmentSessionResponseDto session =
                sessionService.startSession(userId, category);

        return ResponseEntity.ok(session);
    }

    /*
        GET /sessions/{sessionId}
        Fetches session details including questionIds and endTime.
        Used by QuestionPage to load questions and timer.
    */
    @GetMapping("/{sessionId}")
    public ResponseEntity<AssessmentSessionResponseDto> getSession(
            @PathVariable long sessionId
    ) {
        AssessmentSessionResponseDto session =
                sessionService.getSession(sessionId);

        return ResponseEntity.ok(session);
    }
}