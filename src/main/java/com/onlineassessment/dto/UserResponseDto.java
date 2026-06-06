package com.onlineassessment.dto;

import com.onlineassessment.enums.RoleEnum;
import lombok.Data;

@Data
public class UserResponseDto {

    private long id;

    private String name;

    private String email;

    private RoleEnum role;
}