package com.onlineassessment.repository;

import com.onlineassessment.entity.AssessmentSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AssessmentSessionRepository
        extends JpaRepository<AssessmentSession, Long> {
    List<AssessmentSession> findByUserId(long userId);
}