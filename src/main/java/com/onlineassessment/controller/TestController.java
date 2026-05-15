package com.onlineassessment.controller;

import com.onlineassessment.dto.TestDto;
import com.onlineassessment.dto.TestResponseDto;
import com.onlineassessment.service.TestService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tests")
public class TestController {

    @Autowired
    private TestService testService;

    @PostMapping
    public ResponseEntity<TestResponseDto> createTest(
            @Valid @RequestBody TestDto testDto) {

        TestResponseDto test = testService.createTest(testDto);

        return ResponseEntity.ok(test);
    }

    @PostMapping("/{id}/start")
    public ResponseEntity<TestResponseDto> startTest(
            @PathVariable long id) {

        TestResponseDto started = testService.startTest(id);

        return ResponseEntity.ok(started);
    }
}