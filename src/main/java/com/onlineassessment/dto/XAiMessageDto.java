package com.onlineassessment.dto;

import com.onlineassessment.enums.MessageRole;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class XAiMessageDto {
    private long id;
    private MessageRole role;
    private String content;
    private LocalDateTime createdAt;
}
