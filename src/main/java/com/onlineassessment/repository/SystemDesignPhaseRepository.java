package com.onlineassessment.repository;

import com.onlineassessment.entity.SystemDesignPhase;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SystemDesignPhaseRepository
        extends JpaRepository<SystemDesignPhase, Long> {

    List<SystemDesignPhase> findByTrackIdOrderByOrderIndex(long trackId);
}