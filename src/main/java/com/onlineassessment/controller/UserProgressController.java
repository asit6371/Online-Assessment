package com.onlineassessment.controller;

import com.onlineassessment.dto.*;
import com.onlineassessment.service.UserProgressService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/system-design/progress")
public class UserProgressController {

    private final UserProgressService userProgressService;

    public UserProgressController(UserProgressService userProgressService) {
        this.userProgressService = userProgressService;
    }

    // ─── Mark Topic as Visited ────────────────────────────
    @PostMapping("/visit")
    public ResponseEntity<Void> visitTopic(
            @RequestBody VisitTopicRequestDto request
    ) {
        userProgressService.visitTopic(request.getUserId(), request.getTopicId());
        return ResponseEntity.ok().build();
    }

    // ─── Mark Topic as Completed ──────────────────────────
    @PostMapping("/complete")
    public ResponseEntity<Void> completeTopic(
            @RequestBody CompleteTopicRequestDto request
    ) {
        userProgressService.completeTopic(
                request.getUserId(),
                request.getTopicId(),
                request.getDurationMinutes()
        );
        return ResponseEntity.ok().build();
    }

    // ─── Get User Progress Summary ────────────────────────
    @GetMapping("/summary/{userId}")
    public ResponseEntity<UserProgressSummaryDto> getSummary(
            @PathVariable long userId
    ) {
        return ResponseEntity.ok(userProgressService.getSummary(userId));
    }

    // ─── Get Recently Visited Topics ─────────────────────
    @GetMapping("/recent/{userId}")
    public ResponseEntity<List<RecentlyVisitedTopicDto>> getRecentlyVisited(
            @PathVariable long userId
    ) {
        return ResponseEntity.ok(userProgressService.getRecentlyVisited(userId));
    }
}