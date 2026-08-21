package com.onlineassessment.dto;

import com.onlineassessment.enums.TrackType;
import lombok.Data;

@Data
public class SystemDesignTrackRequestDto {
    private TrackType trackType;
    private String name;
    private String description;
    private int totalPhases;
    private int totalTopics;
    private int totalProblems;
    private String estimatedWeeks;
}
