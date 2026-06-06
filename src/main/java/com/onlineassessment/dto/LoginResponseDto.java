package com.onlineassessment.dto;


import com.onlineassessment.enums.RoleEnum;
import lombok.Data;

@Data
public class LoginResponseDto {

    private long userId;
    private String name;
    private String email;
    private String message;
    private String token;
    private RoleEnum role;
}
