package com.onlineassessment.dto;

import lombok.Data;

@Data
public class XAiSendMessageRequestDto {
    private long userId;
    private String message;
}
