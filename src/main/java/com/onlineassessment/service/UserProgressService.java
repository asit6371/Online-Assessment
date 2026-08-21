package com.onlineassessment.service;

import com.onlineassessment.dto.*;
import com.onlineassessment.entity.*;
import com.onlineassessment.enums.TopicStatus;
import com.onlineassessment.exception.UserNotFoundException;
import com.onlineassessment.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserProgressService {

    private final UserTopicProgressRepository progressRepository;
    private final UserLearningSessionRepository sessionRepository;
    private final SystemDesignTopicRepository topicRepository;
    private final SystemDesignTrackRepository trackRepository;
    private final UserRepository userRepository;

    public UserProgressService(
            UserTopicProgressRepository progressRepository,
            UserLearningSessionRepository sessionRepository,
            SystemDesignTopicRepository topicRepository,
            SystemDesignTrackRepository trackRepository,
            UserRepository userRepository
    ) {
        this.progressRepository = progressRepository;
        this.sessionRepository = sessionRepository;
        this.topicRepository = topicRepository;
        this.trackRepository = trackRepository;
        this.userRepository = userRepository;
    }

    // ─── Mark Topic as Visited (IN_PROGRESS) ─────────────

    public void visitTopic(long userId, long topicId) {
        User user = getUser(userId);
        SystemDesignTopic topic = getTopic(topicId);

        Optional<UserTopicProgress> existing =
                progressRepository.findByUserIdAndTopicId(userId, topicId);

        if (existing.isPresent()) {
            // Just update last visited time
            UserTopicProgress progress = existing.get();
            progress.setLastVisitedAt(LocalDateTime.now());
            progressRepository.save(progress);
        } else {
            // First visit — create new progress record
            UserTopicProgress progress = new UserTopicProgress();
            progress.setUser(user);
            progress.setTopic(topic);
            progress.setStatus(TopicStatus.IN_PROGRESS);
            progress.setStartedAt(LocalDateTime.now());
            progress.setLastVisitedAt(LocalDateTime.now());
            progressRepository.save(progress);
        }

        // Start a new learning session
        UserLearningSession session = new UserLearningSession();
        session.setUser(user);
        session.setTopic(topic);
        session.setStartedAt(LocalDateTime.now());
        sessionRepository.save(session);
    }

    // ─── Mark Topic as Completed ──────────────────────────

    public void completeTopic(long userId, long topicId, int durationMinutes) {
        User user = getUser(userId);
        SystemDesignTopic topic = getTopic(topicId);

        Optional<UserTopicProgress> existing =
                progressRepository.findByUserIdAndTopicId(userId, topicId);

        if (existing.isPresent()) {
            UserTopicProgress progress = existing.get();
            // Only update if not already completed
            if (progress.getStatus() != TopicStatus.COMPLETED) {
                progress.setStatus(TopicStatus.COMPLETED);
                progress.setCompletedAt(LocalDateTime.now());
            }
            progress.setLastVisitedAt(LocalDateTime.now());
            progressRepository.save(progress);
        } else {
            // Completed without visiting first
            UserTopicProgress progress = new UserTopicProgress();
            progress.setUser(user);
            progress.setTopic(topic);
            progress.setStatus(TopicStatus.COMPLETED);
            progress.setStartedAt(LocalDateTime.now());
            progress.setCompletedAt(LocalDateTime.now());
            progress.setLastVisitedAt(LocalDateTime.now());
            progressRepository.save(progress);
        }

        // End the most recent open learning session for this topic
        List<UserLearningSession> sessions =
                sessionRepository.findByUserIdOrderByStartedAtDesc(userId);

        sessions.stream()
                .filter(s -> s.getTopic().getId() == topicId && s.getEndedAt() == null)
                .findFirst()
                .ifPresent(s -> {
                    s.setEndedAt(LocalDateTime.now());
                    s.setDurationMinutes(durationMinutes);
                    sessionRepository.save(s);
                });
    }

    // ─── Get User Progress Summary ────────────────────────

    public UserProgressSummaryDto getSummary(long userId) {
        getUser(userId); // validate user exists

        int totalCompleted = progressRepository
                .countByUserIdAndStatus(userId, TopicStatus.COMPLETED);

        int totalTopics = (int) topicRepository.count();

        int totalTimeSpent = sessionRepository.sumDurationByUserId(userId);

        int streak = calculateStreak(userId);

        // LLD vs HLD breakdown
        List<SystemDesignTrack> tracks = trackRepository.findAll();
        int lldCompleted = 0;
        int hldCompleted = 0;

        for (SystemDesignTrack track : tracks) {
            List<Long> trackTopicIds = getTopicIdsForTrack(track.getId());
            long count = progressRepository.findByUserId(userId).stream()
                    .filter(p -> trackTopicIds.contains(p.getTopic().getId()))
                    .filter(p -> p.getStatus() == TopicStatus.COMPLETED)
                    .count();

            switch (track.getTrackType()) {
                case LLD -> lldCompleted = (int) count;
                case HLD -> hldCompleted = (int) count;
            }
        }

        double overallPercent = totalTopics == 0
                ? 0.0
                : Math.round((double) totalCompleted / totalTopics * 10000.0) / 100.0;

        UserProgressSummaryDto dto = new UserProgressSummaryDto();
        dto.setTotalTopicsCompleted(totalCompleted);
        dto.setTotalTopics(totalTopics);
        dto.setTotalTimeSpentMinutes(totalTimeSpent);
        dto.setCurrentStreak(streak);
        dto.setLldCompletedTopics(lldCompleted);
        dto.setHldCompletedTopics(hldCompleted);
        dto.setOverallProgressPercent(overallPercent);
        return dto;
    }

    // ─── Get Recently Visited Topics ─────────────────────

    public List<RecentlyVisitedTopicDto> getRecentlyVisited(long userId) {
        getUser(userId);

        return progressRepository
                .findTop5ByUserIdOrderByLastVisitedAtDesc(userId)
                .stream()
                .map(p -> {
                    SystemDesignTopic topic = p.getTopic();
                    SystemDesignPhase phase = topic.getPhase();
                    SystemDesignTrack track = phase.getTrack();

                    RecentlyVisitedTopicDto dto = new RecentlyVisitedTopicDto();
                    dto.setTopicId(topic.getId());
                    dto.setTopicTitle(topic.getTitle());
                    dto.setPhaseTitle(phase.getTitle());
                    dto.setTrackName(track.getName());
                    dto.setTrackType(track.getTrackType());
                    dto.setStatus(p.getStatus());
                    dto.setLastVisitedAt(p.getLastVisitedAt());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    // ─── Streak Calculation ───────────────────────────────

    private int calculateStreak(long userId) {
        List<UserLearningSession> sessions =
                sessionRepository.findByUserIdOrderByStartedAtDesc(userId);

        if (sessions.isEmpty()) return 0;

        // Get distinct dates when user had a session
        Set<LocalDate> activeDates = sessions.stream()
                .map(s -> s.getStartedAt().toLocalDate())
                .collect(Collectors.toSet());

        List<LocalDate> sortedDates = new ArrayList<>(activeDates);
        Collections.sort(sortedDates, Collections.reverseOrder());

        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);

        // Streak must include today or yesterday to be active
        if (!sortedDates.get(0).equals(today) &&
                !sortedDates.get(0).equals(yesterday)) {
            return 0;
        }

        int streak = 1;
        for (int i = 0; i < sortedDates.size() - 1; i++) {
            if (sortedDates.get(i).minusDays(1).equals(sortedDates.get(i + 1))) {
                streak++;
            } else {
                break;
            }
        }

        return streak;
    }

    // ─── Helper: get all topic IDs for a track ────────────

    private List<Long> getTopicIdsForTrack(long trackId) {
        return topicRepository.findAll().stream()
                .filter(t -> t.getPhase().getTrack().getId() == trackId)
                .map(SystemDesignTopic::getId)
                .collect(Collectors.toList());
    }

    private User getUser(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found!"));
    }

    private SystemDesignTopic getTopic(long topicId) {
        return topicRepository.findById(topicId)
                .orElseThrow(() -> new RuntimeException("Topic not found!"));
    }
}