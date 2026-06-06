package com.onlineassessment.entity;

import com.onlineassessment.enums.AssessmentCategory;
import com.onlineassessment.enums.SessionStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Table(name = "assessment_sessions")
public class AssessmentSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    // Which user owns this session
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Category of assessment (DSA, JAVA_CORE, SPRING_BOOT etc.)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AssessmentCategory category;

    // The 2 randomly picked question IDs for this session
    @ElementCollection
    @CollectionTable(
            name = "session_question_ids",
            joinColumns = @JoinColumn(name = "session_id")
    )
    @Column(name = "question_id")
    private List<Long> questionIds;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SessionStatus status;

    // Duration in minutes — fixed at 60 for now
    @Column(nullable = false)
    private int durationMinutes;
}