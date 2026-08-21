package com.onlineassessment.repository;

import com.onlineassessment.entity.UserLearningSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserLearningSessionRepository
        extends JpaRepository<UserLearningSession, Long> {

    List<UserLearningSession> findByUserId(long userId);

    // Total time spent in minutes across all sessions
    @Query("SELECT COALESCE(SUM(s.durationMinutes), 0) FROM UserLearningSession s WHERE s.user.id = :userId")
    int sumDurationByUserId(long userId);

    // All sessions ordered by date for streak calculation
    @Query("SELECT s FROM UserLearningSession s WHERE s.user.id = :userId ORDER BY s.startedAt DESC")
    List<UserLearningSession> findByUserIdOrderByStartedAtDesc(long userId);
}