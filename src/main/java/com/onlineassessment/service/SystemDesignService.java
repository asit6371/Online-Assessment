package com.onlineassessment.service;

import com.onlineassessment.dto.*;
import com.onlineassessment.entity.*;
import com.onlineassessment.enums.TopicStatus;
import com.onlineassessment.enums.TrackType;
import com.onlineassessment.exception.UserNotFoundException;
import com.onlineassessment.repository.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SystemDesignService {

    private final SystemDesignTrackRepository trackRepository;
    private final SystemDesignPhaseRepository phaseRepository;
    private final SystemDesignTopicRepository topicRepository;
    private final UserTopicProgressRepository progressRepository;
    private final UserRepository userRepository;

    public SystemDesignService(
            SystemDesignTrackRepository trackRepository,
            SystemDesignPhaseRepository phaseRepository,
            SystemDesignTopicRepository topicRepository,
            UserTopicProgressRepository progressRepository,
            UserRepository userRepository
    ) {
        this.trackRepository = trackRepository;
        this.phaseRepository = phaseRepository;
        this.topicRepository = topicRepository;
        this.progressRepository = progressRepository;
        this.userRepository = userRepository;
    }

    // ─── Admin: Create Track ──────────────────────────────

    public SystemDesignTrackResponseDto createTrack(
            SystemDesignTrackRequestDto request
    ) {
        SystemDesignTrack track = new SystemDesignTrack();
        track.setTrackType(request.getTrackType());
        track.setName(request.getName());
        track.setDescription(request.getDescription());
        track.setTotalPhases(request.getTotalPhases());
        track.setTotalTopics(request.getTotalTopics());
        track.setTotalProblems(request.getTotalProblems());
        track.setEstimatedWeeks(request.getEstimatedWeeks());

        SystemDesignTrack saved = trackRepository.save(track);
        return mapTrackToDto(saved, 0, 0.0);
    }

    // ─── Admin: Create Phase ──────────────────────────────

    public SystemDesignPhaseResponseDto createPhase(
            SystemDesignPhaseRequestDto request
    ) {
        SystemDesignTrack track = trackRepository.findById(request.getTrackId())
                .orElseThrow(() -> new RuntimeException("Track not found!"));

        SystemDesignPhase phase = new SystemDesignPhase();
        phase.setTrack(track);
        phase.setPhaseNumber(request.getPhaseNumber());
        phase.setTitle(request.getTitle());
        phase.setSubtitle(request.getSubtitle());
        phase.setOrderIndex(request.getOrderIndex());

        SystemDesignPhase saved = phaseRepository.save(phase);
        return mapPhaseToDto(saved, 0, 0);
    }

    // ─── Admin: Create Topic ──────────────────────────────

    public SystemDesignTopicDetailDto createTopic(
            SystemDesignTopicRequestDto request
    ) {
        SystemDesignPhase phase = phaseRepository.findById(request.getPhaseId())
                .orElseThrow(() -> new RuntimeException("Phase not found!"));

        SystemDesignTopic topic = new SystemDesignTopic();
        topic.setPhase(phase);
        topic.setTitle(request.getTitle());
        topic.setSubtitle(request.getSubtitle());
        topic.setOrderIndex(request.getOrderIndex());
        topic.setOverviewContent(request.getOverviewContent());
        topic.setTheoryContent(request.getTheoryContent());
        topic.setExamplesContent(request.getExamplesContent());
        topic.setDiagramsContent(request.getDiagramsContent());
        topic.setInterviewQaContent(request.getInterviewQaContent());
        topic.setPracticeContent(request.getPracticeContent());

        SystemDesignTopic saved = topicRepository.save(topic);
        return mapTopicToDetailDto(saved, null);
    }

    // ─── Get All Tracks with User Progress ───────────────

    public List<SystemDesignTrackResponseDto> getAllTracks(long userId) {
        List<SystemDesignTrack> tracks = trackRepository.findAll();
        List<SystemDesignTrackResponseDto> result = new ArrayList<>();

        for (SystemDesignTrack track : tracks) {
            int completed = userId > 0
                    ? progressRepository.countCompletedByUserIdAndTrackId(userId, track.getId())
                    : 0;
            double percent = track.getTotalTopics() == 0
                    ? 0.0
                    : Math.round((double) completed / track.getTotalTopics() * 10000.0) / 100.0;
            result.add(mapTrackToDto(track, completed, percent));
        }

        return result;
    }

    // ─── Get Phases for a Track with User Progress ───────

    public List<SystemDesignPhaseResponseDto> getPhasesByTrack(
            long trackId,
            long userId
    ) {
        List<SystemDesignPhase> phases =
                phaseRepository.findByTrackIdOrderByOrderIndex(trackId);

        List<SystemDesignPhaseResponseDto> result = new ArrayList<>();

        for (SystemDesignPhase phase : phases) {
            int totalTopics = topicRepository.countByPhaseId(phase.getId());
            int completedTopics = userId > 0
                    ? progressRepository.findCompletedByUserIdAndPhaseId(userId, phase.getId()).size()
                    : 0;
            result.add(mapPhaseToDto(phase, totalTopics, completedTopics));
        }

        return result;
    }

    // ─── Get Topics for a Phase with User Progress ───────

    public List<SystemDesignTopicSummaryDto> getTopicsByPhase(
            long phaseId,
            long userId
    ) {
        List<SystemDesignTopic> topics =
                topicRepository.findByPhaseIdOrderByOrderIndex(phaseId);

        List<SystemDesignTopicSummaryDto> result = new ArrayList<>();

        for (SystemDesignTopic topic : topics) {
            Optional<UserTopicProgress> progress =
                    progressRepository.findByUserIdAndTopicId(userId, topic.getId());

            TopicStatus status = progress
                    .map(UserTopicProgress::getStatus)
                    .orElse(TopicStatus.NOT_STARTED);

            SystemDesignTopicSummaryDto dto = new SystemDesignTopicSummaryDto();
            dto.setId(topic.getId());
            dto.setTitle(topic.getTitle());
            dto.setSubtitle(topic.getSubtitle());
            dto.setOrderIndex(topic.getOrderIndex());
            dto.setStatus(status);
            result.add(dto);
        }

        return result;
    }

    // ─── Get Full Topic Detail ────────────────────────────

    public SystemDesignTopicDetailDto getTopicDetail(
            long topicId,
            long userId
    ) {
        SystemDesignTopic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new RuntimeException("Topic not found!"));

        Optional<UserTopicProgress> progress =
                progressRepository.findByUserIdAndTopicId(userId, topicId);

        return mapTopicToDetailDto(topic, progress.orElse(null));
    }

    // ─── Get Specific Tab Content ─────────────────────────

    public SystemDesignTopicTabResponseDto getTopicTabContent(
            long topicId,
            String tab
    ) {
        SystemDesignTopic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new RuntimeException("Topic not found!"));

        String content = switch (tab.toUpperCase()) {
            case "OVERVIEW"      -> topic.getOverviewContent();
            case "THEORY"        -> topic.getTheoryContent();
            case "EXAMPLES"      -> topic.getExamplesContent();
            case "DIAGRAMS"      -> topic.getDiagramsContent();
            case "INTERVIEW_QA"  -> topic.getInterviewQaContent();
            case "PRACTICE"      -> topic.getPracticeContent();
            default -> throw new RuntimeException("Invalid tab: " + tab);
        };

        SystemDesignTopicTabResponseDto dto = new SystemDesignTopicTabResponseDto();
        dto.setTopicId(topicId);
        dto.setTab(tab.toUpperCase());
        dto.setContent(content);
        return dto;
    }

    // ─── Mappers ──────────────────────────────────────────

    private SystemDesignTrackResponseDto mapTrackToDto(
            SystemDesignTrack track,
            int completedTopics,
            double progressPercent
    ) {
        SystemDesignTrackResponseDto dto = new SystemDesignTrackResponseDto();
        dto.setId(track.getId());
        dto.setTrackType(track.getTrackType());
        dto.setName(track.getName());
        dto.setDescription(track.getDescription());
        dto.setTotalPhases(track.getTotalPhases());
        dto.setTotalTopics(track.getTotalTopics());
        dto.setTotalProblems(track.getTotalProblems());
        dto.setEstimatedWeeks(track.getEstimatedWeeks());
        dto.setCompletedTopics(completedTopics);
        dto.setProgressPercent(progressPercent);
        return dto;
    }

    private SystemDesignPhaseResponseDto mapPhaseToDto(
            SystemDesignPhase phase,
            int totalTopics,
            int completedTopics
    ) {
        String phaseStatus;
        if (completedTopics == 0) {
            phaseStatus = "NOT_STARTED";
        } else if (completedTopics >= totalTopics && totalTopics > 0) {
            phaseStatus = "COMPLETED";
        } else {
            phaseStatus = "IN_PROGRESS";
        }

        SystemDesignPhaseResponseDto dto = new SystemDesignPhaseResponseDto();
        dto.setId(phase.getId());
        dto.setPhaseNumber(phase.getPhaseNumber());
        dto.setTitle(phase.getTitle());
        dto.setSubtitle(phase.getSubtitle());
        dto.setOrderIndex(phase.getOrderIndex());
        dto.setTotalTopics(totalTopics);
        dto.setCompletedTopics(completedTopics);
        dto.setPhaseStatus(phaseStatus);
        return dto;
    }

    private SystemDesignTopicDetailDto mapTopicToDetailDto(
            SystemDesignTopic topic,
            UserTopicProgress progress
    ) {
        SystemDesignPhase phase = topic.getPhase();
        SystemDesignTrack track = phase.getTrack();

        SystemDesignTopicDetailDto dto = new SystemDesignTopicDetailDto();
        dto.setId(topic.getId());
        dto.setTitle(topic.getTitle());
        dto.setSubtitle(topic.getSubtitle());
        dto.setPhaseId(phase.getId());
        dto.setPhaseTitle(phase.getTitle());
        dto.setPhaseNumber(phase.getPhaseNumber());
        dto.setTrackName(track.getName());
        dto.setTrackType(track.getTrackType());
        dto.setOverviewContent(topic.getOverviewContent());
        dto.setTheoryContent(topic.getTheoryContent());
        dto.setExamplesContent(topic.getExamplesContent());
        dto.setDiagramsContent(topic.getDiagramsContent());
        dto.setInterviewQaContent(topic.getInterviewQaContent());
        dto.setPracticeContent(topic.getPracticeContent());

        if (progress != null) {
            dto.setStatus(progress.getStatus());
            dto.setLastVisitedAt(progress.getLastVisitedAt());
        } else {
            dto.setStatus(TopicStatus.NOT_STARTED);
        }

        return dto;
    }
}