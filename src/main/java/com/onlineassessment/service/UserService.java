package com.onlineassessment.service;

import com.onlineassessment.config.JwtService;
import com.onlineassessment.dto.LoginRequestDto;
import com.onlineassessment.dto.LoginResponseDto;
import com.onlineassessment.dto.UserRequestDto;
import com.onlineassessment.dto.UserResponseDto;
import com.onlineassessment.entity.User;
import com.onlineassessment.enums.RoleEnum;
import com.onlineassessment.exception.UserNotFoundException;
import com.onlineassessment.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    @Autowired
    private JwtService jwtService;


    public UserResponseDto registerUser(UserRequestDto userRequestDto) {
        Optional<User> email = userRepository.findByEmail(userRequestDto.getEmail());

        if (email.isPresent()) {
            throw new RuntimeException("Email already registered");
        }

        User user = new User();

        user.setName(userRequestDto.getName());
        user.setEmail(userRequestDto.getEmail());
        user.setPassword(bCryptPasswordEncoder.encode(userRequestDto.getPassword()));
        user.setRole(RoleEnum.USER);

        User savedUser = userRepository.save(user);


        UserResponseDto responseDto = new UserResponseDto();

        responseDto.setId(savedUser.getId());
        responseDto.setName(savedUser.getName());
        responseDto.setEmail(savedUser.getEmail());
        responseDto.setRole(savedUser.getRole());

        return responseDto;
    }

    public LoginResponseDto loginUser(LoginRequestDto loginRequestDto) {
        Optional<User> byEmail = userRepository.findByEmail(loginRequestDto.getEmail());

        if (byEmail.isEmpty()) {
            throw new UserNotFoundException("Email not found!");
        }

        User user = byEmail.get();

        if (!bCryptPasswordEncoder.matches(loginRequestDto.getPassword(), user.getPassword())) {
            throw new UserNotFoundException("Invalid password!");
        }

        String token = jwtService.generateToken(user.getEmail(), user.getRole().name());

        LoginResponseDto loginResponseDto = new LoginResponseDto();

        loginResponseDto.setUserId(user.getId());
        loginResponseDto.setName(user.getName());
        loginResponseDto.setEmail(user.getEmail());
        loginResponseDto.setRole(user.getRole());
        loginResponseDto.setToken(token);
        loginResponseDto.setMessage("Logged in successfully");

        return loginResponseDto;
    }

    public List<UserResponseDto> getAllUsers() {
        List<User> userList = userRepository.findAll();

        List<UserResponseDto> responseDtoList = new ArrayList<>();

        for (User user : userList) {

            UserResponseDto responseDto = new UserResponseDto();

            responseDto.setId(user.getId());
            responseDto.setName(user.getName());
            responseDto.setEmail(user.getEmail());
            responseDto.setRole(user.getRole());

            responseDtoList.add(responseDto);
        }

        return responseDtoList;
    }
}
