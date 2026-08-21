package com.onlineassessment.repository;

import com.onlineassessment.entity.UserTopicProgress;
import com.onlineassessment.enums.TopicStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserTopicProgressRepository
        extends JpaRepository<UserTopicProgress, Long> {

    Optional<UserTopicProgress> findByUserIdAndTopicId(long userId, long topicId);

    List<UserTopicProgress> findByUserId(long userId);

    int countByUserIdAndStatus(long userId, TopicStatus status);

    // Last 5 visited topics ordered by most recent
    List<UserTopicProgress> findTop5ByUserIdOrderByLastVisitedAtDesc(long userId);

    // All completed topics for a specific phase — used for phase progress
    @Query("""
        SELECT utp FROM UserTopicProgress utp
        WHERE utp.user.id = :userId
        AND utp.topic.phase.id = :phaseId
        AND utp.status = 'COMPLETED'
    """)
    List<UserTopicProgress> findCompletedByUserIdAndPhaseId(long userId, long phaseId);
}