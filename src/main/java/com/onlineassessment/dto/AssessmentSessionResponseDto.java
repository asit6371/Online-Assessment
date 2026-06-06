package com.onlineassessment.dto;

import com.onlineassessment.enums.AssessmentCategory;
import com.onlineassessment.enums.SessionStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class AssessmentSessionResponseDto {

    private long id;

    private long userId;

    private AssessmentCategory category;

    private List<Long> questionIds;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private int durationMinutes;

    private SessionStatus status;
}