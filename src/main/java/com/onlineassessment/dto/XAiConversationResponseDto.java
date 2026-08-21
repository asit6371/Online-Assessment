package com.onlineassessment.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class XAiConversationResponseDto {
    private long id;
    private String title;
    private LocalDateTime createdAt;
    private List<XAiMessageDto> messages;
}