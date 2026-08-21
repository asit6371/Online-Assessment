package com.onlineassessment.repository;

import com.onlineassessment.entity.XAiMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface XAiMessageRepository
        extends JpaRepository<XAiMessage, Long> {

    List<XAiMessage> findByConversationIdOrderByCreatedAtAsc(long conversationId);
}