package com.onlineassessment.entity;

import com.onlineassessment.judge.enums.Verdict;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "submissions")
public class Submission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(
            name = "question_id",
            nullable = false
    )
    private Question question;

    @ManyToOne
    @JoinColumn(
            name = "session_id",
            nullable = false
    )
    private AssessmentSession session;

    @ManyToOne
    @JoinColumn(
            name = "user_id",
            nullable = false
    )
    private User user;

    @Column(
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String code;

    private LocalDateTime submittedAt;

    @Enumerated(EnumType.STRING)
    private Verdict verdict;

    private int passedTestCases;

    private int totalTestCases;
}