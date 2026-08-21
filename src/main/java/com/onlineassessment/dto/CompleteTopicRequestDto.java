package com.onlineassessment.dto;

import lombok.Data;

@Data
public class CompleteTopicRequestDto {
    private long userId;
    private long topicId;
    // Session duration in minutes — sent from frontend when user completes
    private int durationMinutes;
}
