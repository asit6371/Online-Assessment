package com.onlineassessment.service;

import com.onlineassessment.dto.TestDto;
import com.onlineassessment.dto.TestResponseDto;
import com.onlineassessment.entity.Question;
import com.onlineassessment.entity.Test;
import com.onlineassessment.entity.User;
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

    public TestResponseDto createTest(TestDto testDto) {

        Optional<User> existingUser =
                userRepository.findById(testDto.getUserId());

        if (existingUser.isEmpty()) {
            throw new RuntimeException("User not found!");
        }

        User user = existingUser.get();

        List<Question> questionList =
                questionRepository.findAllById(testDto.getQuestionIds());

        if (testDto.getQuestionIds().size() != questionList.size()) {
            throw new RuntimeException("Invalid questions!");
        }

        Test test = new Test();

        test.setUser(user);
        test.setQuestions(questionList);

        Test saved = testRepository.save(test);

        return mapToDto(saved);
    }

    public TestResponseDto startTest(long testId) {

        Optional<Test> id = testRepository.findById(testId);

        if (id.isEmpty()) {
            throw new RuntimeException("Test does not exist!");
        }

        Test currentTest = id.get();

        if (currentTest.getStartTime() != null) {
            throw new RuntimeException("Test has already started!");
        }

        LocalDateTime currTime = LocalDateTime.now();

        currentTest.setStartTime(currTime);
        currentTest.setEndTime(currTime.plusMinutes(45));

        Test updatedTest = testRepository.save(currentTest);

        return mapToDto(updatedTest);
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

        return response;
    }
}