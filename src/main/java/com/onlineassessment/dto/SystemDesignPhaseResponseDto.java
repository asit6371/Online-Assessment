package com.onlineassessment.dto;

import lombok.Data;

@Data
public class SystemDesignPhaseResponseDto {
    private long id;
    private int phaseNumber;
    private String title;
    private String subtitle;
    private int orderIndex;
    private int totalTopics;
    // How many topics this user completed in this phase
    private int completedTopics;
    // NOT_STARTED / IN_PROGRESS / COMPLETED
    private String phaseStatus;
}