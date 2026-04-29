package com.onlineassessment.entity;

import com.onlineassessment.enums.Difficulty;
import com.onlineassessment.enums.Topic;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "questions")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String sampleInput;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String sampleOutput;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String constraints;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Difficulty difficulty;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Topic topic;

    @ManyToMany(mappedBy = "questions")
    private List<Test> test;
}
