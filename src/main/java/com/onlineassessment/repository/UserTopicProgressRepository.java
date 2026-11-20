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

    // Completed count for a specific track by ID
    @Query("""
        SELECT COUNT(utp) FROM UserTopicProgress utp
        WHERE utp.user.id = :userId
        AND utp.topic.phase.track.id = :trackId
        AND utp.status = 'COMPLETED'
    """)
    int countCompletedByUserIdAndTrackId(@org.springframework.data.repository.query.Param("userId") long userId, @org.springframework.data.repository.query.Param("trackId") long trackId);

    // Completed count for a specific track by TrackType (LLD / HLD)
    @Query("""
        SELECT COUNT(utp) FROM UserTopicProgress utp
        WHERE utp.user.id = :userId
        AND utp.topic.phase.track.trackType = :trackType
        AND utp.status = 'COMPLETED'
    """)
    int countCompletedByUserIdAndTrackType(@org.springframework.data.repository.query.Param("userId") long userId, @org.springframework.data.repository.query.Param("trackType") com.onlineassessment.enums.TrackType trackType);

    // All completed topics for a specific track
    @Query("""
        SELECT utp FROM UserTopicProgress utp
        WHERE utp.user.id = :userId
        AND utp.topic.phase.track.id = :trackId
        AND utp.status = 'COMPLETED'
    """)
    List<UserTopicProgress> findCompletedByUserIdAndTrackId(@org.springframework.data.repository.query.Param("userId") long userId, @org.springframework.data.repository.query.Param("trackId") long trackId);

    // Last 5 visited topics ordered by most recent
    List<UserTopicProgress> findTop5ByUserIdOrderByLastVisitedAtDesc(long userId);

    // All completed topics for a specific phase — used for phase progress
    @Query("""
        SELECT utp FROM UserTopicProgress utp
        WHERE utp.user.id = :userId
        AND utp.topic.phase.id = :phaseId
        AND utp.status = 'COMPLETED'
    """)
    List<UserTopicProgress> findCompletedByUserIdAndPhaseId(@org.springframework.data.repository.query.Param("userId") long userId, @org.springframework.data.repository.query.Param("phaseId") long phaseId);
}