package com.onlineassessment.service;

import com.onlineassessment.dto.TestRequestDto;
import com.onlineassessment.dto.TestResponseDto;
import com.onlineassessment.entity.Question;
import com.onlineassessment.entity.Test;
import com.onlineassessment.entity.User;
import com.onlineassessment.exception.QuestionNotFoundException;
import com.onlineassessment.exception.TestNotFoundException;
import com.onlineassessment.exception.UserNotFoundException;
import com.onlineassessment.repository.QuestionRepository;
import com.onlineassessment.repository.TestRepository;
import com.onlineassessment.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TestService {

    @Autowired
    private TestRepository testRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuestionRepository questionRepository;

    public TestResponseDto createTest(TestRequestDto testRequestDto) {

        Optional<User> existingUser =
                userRepository.findById(testRequestDto.getUserId());

        if (existingUser.isEmpty()) {
            throw new UserNotFoundException("User not found!");
        }

        User user = existingUser.get();

        List<Question> questionList =
                questionRepository.findAllById(testRequestDto.getQuestionIds());

        if (testRequestDto.getQuestionIds().size() != questionList.size()) {
            throw new QuestionNotFoundException("Invalid questions!");
        }

        Test test = new Test();

        test.setUser(user);
        test.setQuestions(questionList);
        test.setTitle(testRequestDto.getTitle());
        test.setDescription(testRequestDto.getDescription());
        test.setDurationMinutes(testRequestDto.getDurationMinutes());

        Test saved = testRepository.save(test);

        return mapToDto(saved);
    }

    public TestResponseDto startTest(long testId) {

        Optional<Test> id = testRepository.findById(testId);

        if (id.isEmpty()) {
            throw new TestNotFoundException("Test does not exist!");
        }

        Test currentTest = id.get();

        if (currentTest.getStartTime() != null) {
            throw new TestNotFoundException("Test has already started!");
        }

        LocalDateTime currTime = LocalDateTime.now();

        currentTest.setStartTime(currTime);
        currentTest.setEndTime(currTime.plusMinutes(45));

        Test updatedTest = testRepository.save(currentTest);

        return mapToDto(updatedTest);
    }

    public List<TestResponseDto> getAllTests() {

        List<Test> tests =
                testRepository.findAll();

        List<TestResponseDto> responseList =
                new ArrayList<>();

        for (Test test : tests) {

            responseList.add(
                    mapToDto(test)
            );
        }

        return responseList;
    }

    public TestResponseDto getTestById(
            long testId
    ) {

        Test test =
                testRepository.findById(testId)
                        .orElseThrow(() ->
                                new TestNotFoundException(
                                        "Test not found!"
                                )
                        );

        return mapToDto(test);
    }

    private TestResponseDto mapToDto(Test test) {

        TestResponseDto response = new TestResponseDto();

        response.setId(test.getId());

        response.setUserId(test.getUser().getId());

        List<Long> questionIds = new ArrayList<>();

        for (Question question : test.getQuestions()) {
            questionIds.add(question.getId());
        }

        response.setQuestionIds(questionIds);

        response.setStartTime(test.getStartTime());

        response.setEndTime(test.getEndTime());

        response.setTitle(test.getTitle());

        response.setDescription(test.getDescription());

        response.setDurationMinutes(test.getDurationMinutes());

        return response;
    }
}