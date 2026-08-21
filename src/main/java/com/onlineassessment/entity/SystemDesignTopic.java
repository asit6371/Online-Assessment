package com.onlineassessment.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "system_design_topics")
public class SystemDesignTopic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "phase_id", nullable = false)
    private SystemDesignPhase phase;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String subtitle;

    // Controls display order within a phase
    @Column(nullable = false)
    private int orderIndex;

    // Tab contents — all written by admin
    @Column(columnDefinition = "TEXT")
    private String overviewContent;

    @Column(columnDefinition = "TEXT")
    private String theoryContent;

    @Column(columnDefinition = "TEXT")
    private String examplesContent;

    @Column(columnDefinition = "TEXT")
    private String diagramsContent;

    @Column(columnDefinition = "TEXT")
    private String interviewQaContent;

    @Column(columnDefinition = "TEXT")
    private String practiceContent;
}