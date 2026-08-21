package com.onlineassessment.dto;

import lombok.Data;

@Data
public class SystemDesignTopicRequestDto {
    private long phaseId;
    private String title;
    private String subtitle;
    private int orderIndex;
    private String overviewContent;
    private String theoryContent;
    private String examplesContent;
    private String diagramsContent;
    private String interviewQaContent;
    private String practiceContent;
}
