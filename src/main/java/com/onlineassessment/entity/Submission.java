package com.onlineassessment.entity;

import com.onlineassessment.enums.Status;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "submissions")
public class Submission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @ManyToOne
    @JoinColumn(name = "test_id", nullable = false)
    private Test test;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String code;

    @Column(nullable = false)
    private Status status;
}
