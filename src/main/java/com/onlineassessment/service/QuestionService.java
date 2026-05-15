package com.onlineassessment.service;

import com.onlineassessment.dto.QuestionRequestDto;
import com.onlineassessment.dto.QuestionResponseDto;
import com.onlineassessment.entity.Question;
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
            QuestionRequestDto questionRequestDto) {

        if (questionRepository.existsByTitle(
                questionRequestDto.getTitle())) {

            throw new RuntimeException(
                    "Question title already exists");
        }

        Question question = new Question();

        question.setTitle(questionRequestDto.getTitle());
        question.setDescription(questionRequestDto.getDescription());
        question.setSampleInput(questionRequestDto.getSampleInput());
        question.setSampleOutput(questionRequestDto.getSampleOutput());
        question.setConstraints(questionRequestDto.getConstraints());
        question.setDifficulty(questionRequestDto.getDifficulty());
        question.setTopic(questionRequestDto.getTopic());

        Question saved = questionRepository.save(question);

        return mapToDto(saved);
    }

    public List<QuestionResponseDto> getAllQuestion() {

        List<Question> findAllQuestions =
                questionRepository.findAll();

        List<QuestionResponseDto> responseList =
                new ArrayList<>();

        for (Question question : findAllQuestions) {

            responseList.add(mapToDto(question));
        }

        return responseList;
    }

    public QuestionResponseDto getQuestionById(long id) {

        Optional<Question> question =
                questionRepository.findById(id);

        if (question.isEmpty()) {
            throw new RuntimeException(
                    "Question does not exist!");
        }

        return mapToDto(question.get());
    }

    private QuestionResponseDto mapToDto(Question question) {

        QuestionResponseDto response =
                new QuestionResponseDto();

        response.setId(question.getId());
        response.setTitle(question.getTitle());
        response.setDescription(question.getDescription());
        response.setSampleInput(question.getSampleInput());
        response.setSampleOutput(question.getSampleOutput());
        response.setConstraints(question.getConstraints());
        response.setDifficulty(question.getDifficulty());
        response.setTopic(question.getTopic());

        return response;
    }
}