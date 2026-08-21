package com.onlineassessment.controller;

import com.onlineassessment.dto.*;
import com.onlineassessment.service.XAiService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/xai")
public class XAiController {

    private final XAiService xAiService;

    public XAiController(XAiService xAiService) {
        this.xAiService = xAiService;
    }

    // ─── Get All Conversations for User ──────────────────
    @GetMapping("/conversations/{userId}")
    public ResponseEntity<List<XAiConversationResponseDto>> getConversations(
            @PathVariable long userId
    ) {
        return ResponseEntity.ok(xAiService.getConversations(userId));
    }

    // ─── Get Single Conversation with Messages ────────────
    @GetMapping("/conversation/{conversationId}")
    public ResponseEntity<XAiConversationResponseDto> getConversation(
            @PathVariable long conversationId
    ) {
        return ResponseEntity.ok(xAiService.getConversation(conversationId));
    }

    // ─── Start New Conversation ───────────────────────────
    @PostMapping("/conversations/new")
    public ResponseEntity<XAiConversationResponseDto> startConversation(
            @RequestBody XAiNewConversationRequestDto request
    ) {
        return ResponseEntity.ok(
                xAiService.startConversation(
                        request.getUserId(),
                        request.getFirstMessage()
                )
        );
    }

    // ─── Send Message in Existing Conversation ────────────
    @PostMapping("/conversations/{conversationId}/message")
    public ResponseEntity<XAiConversationResponseDto> sendMessage(
            @PathVariable long conversationId,
            @RequestBody XAiSendMessageRequestDto request
    ) {
        return ResponseEntity.ok(
                xAiService.sendMessage(
                        conversationId,
                        request.getUserId(),
                        request.getMessage()
                )
        );
    }

    // ─── Delete Conversation ──────────────────────────────
    @DeleteMapping("/conversations/{conversationId}")
    public ResponseEntity<Void> deleteConversation(
            @PathVariable long conversationId
    ) {
        xAiService.deleteConversation(conversationId);
        return ResponseEntity.ok().build();
    }
}