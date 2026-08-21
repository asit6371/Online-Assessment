package com.onlineassessment.repository;

import com.onlineassessment.entity.XAiConversation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface XAiConversationRepository
        extends JpaRepository<XAiConversation, Long> {

    List<XAiConversation> findByUserIdOrderByCreatedAtDesc(long userId);
}