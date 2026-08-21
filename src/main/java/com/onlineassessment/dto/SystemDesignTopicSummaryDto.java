package com.onlineassessment.dto;

import com.onlineassessment.enums.TopicStatus;
import lombok.Data;

@Data
public class SystemDesignTopicSummaryDto {
    private long id;
    private String title;
    private String subtitle;
    private int orderIndex;
    private TopicStatus status; // user's progress on this topic
}
