package com.onlineassessment.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class TestResponseDto {

    private long id;

    private long userId;

    private List<Long> questionIds;

    private LocalDateTime startTime;

    private LocalDateTime endTime;
}