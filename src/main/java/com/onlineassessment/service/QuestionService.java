package com.onlineassessment.service;

import com.onlineassessment.dto.QuestionRequestDto;
import com.onlineassessment.dto.QuestionResponseDto;
import com.onlineassessment.dto.TestCaseDto;
import com.onlineassessment.entity.Question;
import com.onlineassessment.entity.TestCase;
import com.onlineassessment.exception.QuestionNotFoundException;
import com.onlineassessment.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    public QuestionResponseDto createQuestion(
            QuestionRequestDto questionRequestDto
    ) {
        if (questionRepository.existsByTitle(
                questionRequestDto.getTitle())) {
            throw new QuestionNotFoundException(
                    "Question title already exists"
            );
        }

        Question question = new Question();

        question.setTitle(questionRequestDto.getTitle());
        question.setDescription(questionRequestDto.getDescription());
        question.setSampleInput(questionRequestDto.getSampleInput());
        question.setSampleOutput(questionRequestDto.getSampleOutput());
        question.setConstraints(questionRequestDto.getConstraints());
        question.setDifficulty(questionRequestDto.getDifficulty());
        question.setTopic(questionRequestDto.getTopic());
        question.setStarterCode(questionRequestDto.getStarterCode());
        question.setDriverCode(questionRequestDto.getDriverCode());

        List<TestCase> testCases = new ArrayList<>();

        for (TestCaseDto dto : questionRequestDto.getTestCases()) {
            TestCase testCase = new TestCase();
            testCase.setInput(dto.getInput());
            testCase.setExpectedOutput(dto.getExpectedOutput());
            testCase.setHidden(dto.isHidden());

            // IMPORTANT: set owning side of relationship
            testCase.setQuestion(question);
            testCases.add(testCase);
        }

        question.setTestCases(testCases);

        Question saved = questionRepository.save(question);

        return mapToDto(saved);
    }

    public List<QuestionResponseDto> getAllQuestion() {
        List<Question> all = questionRepository.findAll();
        List<QuestionResponseDto> result = new ArrayList<>();
        for (Question q : all) {
            result.add(mapToDto(q));
        }
        return result;
    }

    public QuestionResponseDto getQuestionById(long id) {
        Optional<Question> question = questionRepository.findById(id);
        if (question.isEmpty()) {
            throw new QuestionNotFoundException("Question does not exist!");
        }
        return mapToDto(question.get());
    }

    public Question getQuestionEntityById(long id) {
        return questionRepository
                .findById(id)
                .orElseThrow(() ->
                        new QuestionNotFoundException("Question not found!")
                );
    }

    private QuestionResponseDto mapToDto(Question question) {
        QuestionResponseDto response = new QuestionResponseDto();

        response.setId(question.getId());
        response.setTitle(question.getTitle());
        response.setDescription(question.getDescription());
        response.setSampleInput(question.getSampleInput());
        response.setSampleOutput(question.getSampleOutput());
        response.setConstraints(question.getConstraints());
        response.setDifficulty(question.getDifficulty());
        response.setTopic(question.getTopic());

        // starterCode goes to frontend — driverCode stays on backend only
        response.setStarterCode(question.getStarterCode());

        return response;
    }
}