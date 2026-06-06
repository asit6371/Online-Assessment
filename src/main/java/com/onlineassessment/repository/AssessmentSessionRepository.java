package com.onlineassessment.repository;

import com.onlineassessment.entity.AssessmentSession;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssessmentSessionRepository
        extends JpaRepository<AssessmentSession, Long> {
}