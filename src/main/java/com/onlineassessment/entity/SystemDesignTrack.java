package com.onlineassessment.entity;

import com.onlineassessment.enums.TrackType;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "system_design_tracks")
public class SystemDesignTrack {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private TrackType trackType;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private int totalPhases;

    @Column(nullable = false)
    private int totalTopics;

    @Column(nullable = false)
    private int totalProblems;

    @Column(nullable = false)
    private String estimatedWeeks;
}