package com.onlineassessment.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class TestCase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(columnDefinition = "TEXT")
    private String input;

    @Column(columnDefinition = "TEXT")
    private String expectedOutput;

    private boolean hidden;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;
}