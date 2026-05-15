package com.onlineassessment.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.onlineassessment.enums.Difficulty;
import com.onlineassessment.enums.Topic;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "questions")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Title cannot be empty")
    @Column(nullable = false)
    private String title;

    @NotBlank(message = "Description cannot be empty")
    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @NotBlank(message = "sampleInput cannot be blank")
    @Column(nullable = false, columnDefinition = "TEXT")
    private String sampleInput;

    @NotBlank(message = "sampleOutput cannot be blank")
    @Column(nullable = false, columnDefinition = "TEXT")
    private String sampleOutput;

    @NotBlank(message = "Constraints cannot be empty")
    @Column(nullable = false, columnDefinition = "TEXT")
    private String constraints;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Difficulty difficulty;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Topic topic;

    @JsonIgnore
    @ManyToMany(mappedBy = "questions")
    private List<Test> tests;
}
