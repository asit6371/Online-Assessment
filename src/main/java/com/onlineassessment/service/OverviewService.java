package com.onlineassessment.service;

import com.onlineassessment.dto.OverviewResponseDto;
import com.onlineassessment.entity.AssessmentSession;
import com.onlineassessment.entity.Question;
import com.onlineassessment.entity.Submission;
import com.onlineassessment.exception.UserNotFoundException;
import com.onlineassessment.judge.enums.Verdict;
import com.onlineassessment.repository.AssessmentSessionRepository;
import com.onlineassessment.repository.SubmissionRepository;
import com.onlineassessment.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OverviewService {

    private final SubmissionRepository submissionRepository;
    private final AssessmentSessionRepository sessionRepository;
    private final UserRepository userRepository;

    public OverviewService(
            SubmissionRepository submissionRepository,
            AssessmentSessionRepository sessionRepository,
            UserRepository userRepository
    ) {
        this.submissionRepository = submissionRepository;
        this.sessionRepository = sessionRepository;
        this.userRepository = userRepository;
    }

    public OverviewResponseDto getOverview(long userId) {

        // Validate user exists
        userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found!"));

        // Fetch all data for this user
        List<Submission> allSubmissions =
                submissionRepository.findByUserId(userId);

        List<AssessmentSession> allSessions =
                sessionRepository.findByUserId(userId);

        OverviewResponseDto dto = new OverviewResponseDto();

        // ── At a Glance ──
        dto.setTotalSessions(allSessions.size());
        dto.setTotalSubmitted(allSubmissions.size());

        long acceptedCount = allSubmissions.stream()
                .filter(s -> s.getVerdict() == Verdict.ACCEPTED)
                .count();
        dto.setTotalAccepted((int) acceptedCount);
        dto.setTotalQuestionsAttempted(allSubmissions.size());

        // Overall pass rate
        double passRate = allSubmissions.isEmpty()
                ? 0.0
                : (double) acceptedCount / allSubmissions.size() * 100;
        dto.setOverallPassRate(Math.round(passRate * 100.0) / 100.0);

        // ── Best Streak ──
        dto.setBestStreak(calculateBestStreak(allSubmissions));

        // ── Topic Performance ──
        dto.setTopicPerformance(calculateTopicPerformance(allSubmissions));

        // ── Recent Sessions — last 5 ──
        dto.setRecentSessions(calculateRecentSessions(allSessions, allSubmissions));

        return dto;
    }

    private int calculateBestStreak(List<Submission> submissions) {
        if (submissions.isEmpty()) return 0;

        // Get distinct dates where user had at least one ACCEPTED submission
        Set<LocalDate> activeDates = submissions.stream()
                .filter(s -> s.getVerdict() == Verdict.ACCEPTED)
                .map(s -> s.getSubmittedAt().toLocalDate())
                .collect(Collectors.toSet());

        if (activeDates.isEmpty()) return 0;

        List<LocalDate> sortedDates = new ArrayList<>(activeDates);
        Collections.sort(sortedDates);

        int bestStreak = 1;
        int currentStreak = 1;

        for (int i = 1; i < sortedDates.size(); i++) {
            if (sortedDates.get(i).equals(sortedDates.get(i - 1).plusDays(1))) {
                currentStreak++;
                bestStreak = Math.max(bestStreak, currentStreak);
            } else {
                currentStreak = 1;
            }
        }

        return bestStreak;
    }

    private List<OverviewResponseDto.TopicPerformanceDto> calculateTopicPerformance(
            List<Submission> submissions
    ) {
        // Group submissions by topic
        Map<String, List<Submission>> byTopic = submissions.stream()
                .collect(Collectors.groupingBy(s -> {
                    Question q = s.getQuestion();
                    return q != null ? q.getTopic().name() : "UNKNOWN";
                }));

        List<OverviewResponseDto.TopicPerformanceDto> result = new ArrayList<>();

        for (Map.Entry<String, List<Submission>> entry : byTopic.entrySet()) {
            String topic = entry.getKey();
            List<Submission> topicSubs = entry.getValue();

            int attempted = topicSubs.size();
            int accepted = (int) topicSubs.stream()
                    .filter(s -> s.getVerdict() == Verdict.ACCEPTED)
                    .count();

            double rate = attempted == 0
                    ? 0.0
                    : Math.round((double) accepted / attempted * 10000.0) / 100.0;

            OverviewResponseDto.TopicPerformanceDto topicDto =
                    new OverviewResponseDto.TopicPerformanceDto();
            topicDto.setTopic(topic);
            topicDto.setAttempted(attempted);
            topicDto.setAccepted(accepted);
            topicDto.setPassRate(rate);

            result.add(topicDto);
        }

        // Sort by attempted descending
        result.sort((a, b) -> b.getAttempted() - a.getAttempted());

        return result;
    }

    private List<OverviewResponseDto.RecentSessionDto> calculateRecentSessions(
            List<AssessmentSession> sessions,
            List<Submission> allSubmissions
    ) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");

        // Sort sessions by startTime descending — most recent first
        List<AssessmentSession> sorted = sessions.stream()
                .sorted((a, b) -> b.getStartTime().compareTo(a.getStartTime()))
                .limit(5)
                .collect(Collectors.toList());

        List<OverviewResponseDto.RecentSessionDto> result = new ArrayList<>();

        for (AssessmentSession session : sorted) {
            // Find submissions that belong to this session's questions
            List<Long> sessionQuestionIds = session.getQuestionIds();

            // Get submissions for this user during this session's time window
            List<Submission> sessionSubs = allSubmissions.stream()
                    .filter(s -> sessionQuestionIds.contains(s.getQuestion().getId()))
                    .filter(s -> {
                        // submission was made during this session's time window
                        return !s.getSubmittedAt().isBefore(session.getStartTime())
                                && !s.getSubmittedAt().isAfter(session.getEndTime());
                    })
                    .collect(Collectors.toList());

            int accepted = (int) sessionSubs.stream()
                    .filter(s -> s.getVerdict() == Verdict.ACCEPTED)
                    .count();

            OverviewResponseDto.RecentSessionDto sessionDto =
                    new OverviewResponseDto.RecentSessionDto();
            sessionDto.setDate(session.getStartTime().format(formatter));
            sessionDto.setQuestions(sessionQuestionIds.size());
            sessionDto.setAccepted(accepted);
            sessionDto.setTotal(sessionQuestionIds.size());

            result.add(sessionDto);
        }

        return result;
    }
}