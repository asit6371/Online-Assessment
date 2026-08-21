package com.onlineassessment.entity;

import com.onlineassessment.enums.TopicStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(
        name = "user_topic_progress",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "topic_id"})
)
public class UserTopicProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "topic_id", nullable = false)
    private SystemDesignTopic topic;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TopicStatus status;

    private LocalDateTime startedAt;

    private LocalDateTime completedAt;

    // Updated every time user visits the topic
    private LocalDateTime lastVisitedAt;
}