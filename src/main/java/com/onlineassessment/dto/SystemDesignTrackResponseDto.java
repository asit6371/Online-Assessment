package com.onlineassessment.dto;

import com.onlineassessment.enums.TrackType;
import lombok.Data;

@Data
public class SystemDesignTrackResponseDto {
    private long id;
    private TrackType trackType;
    private String name;
    private String description;
    private int totalPhases;
    private int totalTopics;
    private int totalProblems;
    private String estimatedWeeks;
    // User-specific progress — 0 if not logged in
    private int completedTopics;
    private double progressPercent;
}