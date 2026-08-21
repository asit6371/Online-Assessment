package com.onlineassessment.repository;

import com.onlineassessment.entity.SystemDesignTrack;
import com.onlineassessment.enums.TrackType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SystemDesignTrackRepository
        extends JpaRepository<SystemDesignTrack, Long> {
   SystemDesignTrack findByTrackType(TrackType trackType);
}