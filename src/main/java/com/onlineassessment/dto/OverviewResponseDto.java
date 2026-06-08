package com.onlineassessment.dto;

import lombok.Data;

import java.util.List;

@Data
public class OverviewResponseDto {

    private int totalSessions;

    private int totalQuestionsAttempted;

    private int totalAccepted;

    private int totalSubmitted;

    private int bestStreak;

    private double overallPassRate;

    private List<TopicPerformanceDto> topicPerformance;

    private List<RecentSessionDto> recentSessions;

    @Data
    public static class TopicPerformanceDto {
        private String topic;
        private int attempted;
        private int accepted;
        private double passRate;
    }

    @Data
    public static class RecentSessionDto {
        private String date;
        private int questions;
        private int accepted;
        private int total;
    }
}