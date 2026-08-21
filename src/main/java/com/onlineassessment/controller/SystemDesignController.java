package com.onlineassessment.controller;

import com.onlineassessment.dto.*;
import com.onlineassessment.service.SystemDesignService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/system-design")
public class SystemDesignController {

    private final SystemDesignService systemDesignService;

    public SystemDesignController(SystemDesignService systemDesignService) {
        this.systemDesignService = systemDesignService;
    }

    // ─── Admin: Create Track ──────────────────────────────
    @PostMapping("/tracks")
    public ResponseEntity<SystemDesignTrackResponseDto> createTrack(
            @RequestBody SystemDesignTrackRequestDto request
    ) {
        return ResponseEntity.ok(systemDesignService.createTrack(request));
    }

    // ─── Admin: Create Phase ──────────────────────────────
    @PostMapping("/phases")
    public ResponseEntity<SystemDesignPhaseResponseDto> createPhase(
            @RequestBody SystemDesignPhaseRequestDto request
    ) {
        return ResponseEntity.ok(systemDesignService.createPhase(request));
    }

    // ─── Admin: Create Topic ──────────────────────────────
    @PostMapping("/topics")
    public ResponseEntity<SystemDesignTopicDetailDto> createTopic(
            @RequestBody SystemDesignTopicRequestDto request
    ) {
        return ResponseEntity.ok(systemDesignService.createTopic(request));
    }

    // ─── Get All Tracks with User Progress ───────────────
    @GetMapping("/tracks")
    public ResponseEntity<List<SystemDesignTrackResponseDto>> getAllTracks(
            @RequestParam long userId
    ) {
        return ResponseEntity.ok(systemDesignService.getAllTracks(userId));
    }

    // ─── Get Phases for a Track ───────────────────────────
    @GetMapping("/tracks/{trackId}/phases")
    public ResponseEntity<List<SystemDesignPhaseResponseDto>> getPhasesByTrack(
            @PathVariable long trackId,
            @RequestParam long userId
    ) {
        return ResponseEntity.ok(
                systemDesignService.getPhasesByTrack(trackId, userId)
        );
    }

    // ─── Get Topics for a Phase ───────────────────────────
    @GetMapping("/phases/{phaseId}/topics")
    public ResponseEntity<List<SystemDesignTopicSummaryDto>> getTopicsByPhase(
            @PathVariable long phaseId,
            @RequestParam long userId
    ) {
        return ResponseEntity.ok(
                systemDesignService.getTopicsByPhase(phaseId, userId)
        );
    }

    // ─── Get Full Topic Detail ────────────────────────────
    @GetMapping("/topics/{topicId}")
    public ResponseEntity<SystemDesignTopicDetailDto> getTopicDetail(
            @PathVariable long topicId,
            @RequestParam long userId
    ) {
        return ResponseEntity.ok(
                systemDesignService.getTopicDetail(topicId, userId)
        );
    }

    // ─── Get Specific Tab Content ─────────────────────────
    @GetMapping("/topics/{topicId}/tab/{tab}")
    public ResponseEntity<SystemDesignTopicTabResponseDto> getTopicTabContent(
            @PathVariable long topicId,
            @PathVariable String tab
    ) {
        return ResponseEntity.ok(
                systemDesignService.getTopicTabContent(topicId, tab)
        );
    }
}