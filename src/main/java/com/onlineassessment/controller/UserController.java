package com.onlineassessment.controller;

import com.onlineassessment.dto.LoginRequestDto;
import com.onlineassessment.dto.LoginResponseDto;
import com.onlineassessment.dto.UserRequestDto;
import com.onlineassessment.dto.UserResponseDto;
import com.onlineassessment.entity.User;
import com.onlineassessment.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> loginUser(@Valid @RequestBody
                                                      LoginRequestDto loginRequestDto) {

        LoginResponseDto loginedUser = userService.loginUser(loginRequestDto);

        return ResponseEntity.ok(loginedUser);
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> register(@Valid @RequestBody UserRequestDto userRequestDto) {
        UserResponseDto savedUser = userService.registerUser(userRequestDto);

        return ResponseEntity.ok(savedUser);
    }

    @GetMapping("/all_users")
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        List<UserResponseDto> allUsers = userService.getAllUsers();

        return ResponseEntity.ok(allUsers);
    }
}
