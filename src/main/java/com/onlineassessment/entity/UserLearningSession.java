package com.onlineassessment.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "user_learning_sessions")
public class UserLearningSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "topic_id", nullable = false)
    private SystemDesignTopic topic;

    @Column(nullable = false)
    private LocalDateTime startedAt;

    // Set when user leaves or marks complete
    private LocalDateTime endedAt;

    // Calculated when session ends
    private int durationMinutes;
}