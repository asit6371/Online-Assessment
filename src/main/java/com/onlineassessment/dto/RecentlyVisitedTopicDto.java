package com.onlineassessment.dto;

import com.onlineassessment.enums.TopicStatus;
import com.onlineassessment.enums.TrackType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RecentlyVisitedTopicDto {
    private long topicId;
    private String topicTitle;
    private String phaseTitle;
    private String trackName;
    private TrackType trackType;
    private TopicStatus status;
    private LocalDateTime lastVisitedAt;
}