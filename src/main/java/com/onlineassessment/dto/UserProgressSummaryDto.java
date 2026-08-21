package com.onlineassessment.dto;

import lombok.Data;

@Data
public class UserProgressSummaryDto {
    private int totalTopicsCompleted;
    private int totalTopics;
    private int totalTimeSpentMinutes;
    private int currentStreak; // consecutive days
    private int lldCompletedTopics;
    private int hldCompletedTopics;
    private double overallProgressPercent;
}
