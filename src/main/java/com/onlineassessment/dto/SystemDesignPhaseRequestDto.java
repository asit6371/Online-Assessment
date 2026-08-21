package com.onlineassessment.dto;

import lombok.Data;

@Data
public class SystemDesignPhaseRequestDto {
    private long trackId;
    private int phaseNumber;
    private String title;
    private String subtitle;
    private int orderIndex;
}

