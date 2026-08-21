package com.onlineassessment.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "system_design_phases")
public class SystemDesignPhase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "track_id", nullable = false)
    private SystemDesignTrack track;

    @Column(nullable = false)
    private int phaseNumber;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String subtitle;

    // Controls display order within a track
    @Column(nullable = false)
    private int orderIndex;
}