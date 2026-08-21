package com.onlineassessment.repository;

import com.onlineassessment.entity.SystemDesignTopic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SystemDesignTopicRepository
        extends JpaRepository<SystemDesignTopic, Long> {

    List<SystemDesignTopic> findByPhaseIdOrderByOrderIndex(long phaseId);

    int countByPhaseId(long phaseId);
}