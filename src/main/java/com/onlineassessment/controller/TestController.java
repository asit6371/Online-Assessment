package com.onlineassessment.controller;

import com.onlineassessment.dto.TestRequestDto;
import com.onlineassessment.dto.TestResponseDto;
import com.onlineassessment.service.TestService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tests")
public class TestController {

    @Autowired
    private TestService testService;

    @PostMapping
    public ResponseEntity<TestResponseDto> createTest(
            @Valid @RequestBody TestRequestDto testRequestDto) {

        TestResponseDto test = testService.createTest(testRequestDto);

        return ResponseEntity.ok(test);
    }

    @PostMapping("/{id}/start")
    public ResponseEntity<TestResponseDto> startTest(
            @PathVariable long id) {

        TestResponseDto started = testService.startTest(id);

        return ResponseEntity.ok(started);
    }

    @GetMapping
    public ResponseEntity<List<TestResponseDto>>
    getAllTests() {

        List<TestResponseDto> tests =
                testService.getAllTests();

        return ResponseEntity.ok(tests);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TestResponseDto>
    getTestById(
            @PathVariable long id
    ) {

        TestResponseDto test =
                testService.getTestById(id);

        return ResponseEntity.ok(test);
    }
}