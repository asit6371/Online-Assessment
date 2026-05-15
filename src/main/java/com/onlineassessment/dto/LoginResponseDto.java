package com.onlineassessment.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class LoginResponseDto {

    private long userId;
    private String name;
    private String email;
    private String message;
}
