package com.onlineassessment.dto;

import lombok.Data;
import org.springframework.web.bind.annotation.ControllerAdvice;
import java.time.LocalDateTime;

@Data
public class ErrorResponseDto {

    private String message;

    private int status;

    private LocalDateTime time;
}
