package com.onlineassessment.dto;

import com.onlineassessment.enums.TopicStatus;
import com.onlineassessment.enums.TrackType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SystemDesignTopicDetailDto {
    private long id;
    private String title;
    private String subtitle;
    private long phaseId;
    private String phaseTitle;
    private int phaseNumber;
    private String trackName;
    private TrackType trackType;
    // All tab contents
    private String overviewContent;
    private String theoryContent;
    private String examplesContent;
    private String diagramsContent;
    private String interviewQaContent;
    private String practiceContent;
    // User progress
    private TopicStatus status;
    private LocalDateTime lastVisitedAt;
}