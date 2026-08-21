package com.onlineassessment.dto;

import lombok.Data;

@Data
public class XAiNewConversationRequestDto {
    private long userId;
    private String firstMessage;
}
