package com.onlineassessment.service;

import com.onlineassessment.dto.XAiConversationResponseDto;
import com.onlineassessment.dto.XAiMessageDto;
import com.onlineassessment.entity.User;
import com.onlineassessment.entity.XAiConversation;
import com.onlineassessment.entity.XAiMessage;
import com.onlineassessment.enums.MessageRole;
import com.onlineassessment.exception.UserNotFoundException;
import com.onlineassessment.repository.UserRepository;
import com.onlineassessment.repository.XAiConversationRepository;
import com.onlineassessment.repository.XAiMessageRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class XAiService {

    private final XAiConversationRepository conversationRepository;
    private final XAiMessageRepository messageRepository;
    private final UserRepository userRepository;
    private final RestTemplate restTemplate;

    @Value("${groq.api.key}")
    private String openAiApiKey;

    private static final String OPENAI_URL =
            "https://api.groq.com/openai/v1/chat/completions";

    private static final String SYSTEM_PROMPT =
            "You are X AI, an expert System Design mentor for the JudgeX platform. " +
                    "You specialize in Low-Level Design (LLD) and High-Level Design (HLD). " +
                    "Your role is to help developers understand system design concepts, " +
                    "design patterns, OOP principles, scalable architectures, databases, " +
                    "and real-world system design problems. " +
                    "Always provide clear, structured answers with examples where possible. " +
                    "When explaining code, use Java examples. " +
                    "Keep your tone professional but friendly.";

    public XAiService(
            XAiConversationRepository conversationRepository,
            XAiMessageRepository messageRepository,
            UserRepository userRepository
    ) {
        this.conversationRepository = conversationRepository;
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.restTemplate = new RestTemplate();
    }

    // ─── Get All Conversations for a User ────────────────

    public List<XAiConversationResponseDto> getConversations(long userId) {
        getUser(userId);

        return conversationRepository
                .findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(c -> mapToDto(c, false)) // no messages in list view
                .collect(Collectors.toList());
    }

    // ─── Get Single Conversation with Messages ────────────

    public XAiConversationResponseDto getConversation(long conversationId) {
        XAiConversation conversation = conversationRepository
                .findById(conversationId)
                .orElseThrow(() -> new RuntimeException("Conversation not found!"));

        return mapToDto(conversation, true);
    }

    // ─── Start New Conversation ───────────────────────────

    public XAiConversationResponseDto startConversation(
            long userId,
            String firstMessage
    ) {
        User user = getUser(userId);

        // Create conversation — title from first 60 chars of first message
        XAiConversation conversation = new XAiConversation();
        conversation.setUser(user);
        conversation.setTitle(
                firstMessage.length() > 60
                        ? firstMessage.substring(0, 60) + "..."
                        : firstMessage
        );
        conversation.setCreatedAt(LocalDateTime.now());
        XAiConversation saved = conversationRepository.save(conversation);

        // Save user message
        XAiMessage userMsg = new XAiMessage();
        userMsg.setConversation(saved);
        userMsg.setRole(MessageRole.USER);
        userMsg.setContent(firstMessage);
        userMsg.setCreatedAt(LocalDateTime.now());
        messageRepository.save(userMsg);

        // Get AI response — no history yet, just first message
        String aiResponse = callOpenAi(
                List.of(userMsg),
                firstMessage
        );

        // Save AI response
        XAiMessage aiMsg = new XAiMessage();
        aiMsg.setConversation(saved);
        aiMsg.setRole(MessageRole.ASSISTANT);
        aiMsg.setContent(aiResponse);
        aiMsg.setCreatedAt(LocalDateTime.now());
        messageRepository.save(aiMsg);

        return mapToDto(saved, true);
    }

    // ─── Send Message in Existing Conversation ────────────

    public XAiConversationResponseDto sendMessage(
            long conversationId,
            long userId,
            String userMessage
    ) {
        getUser(userId);

        XAiConversation conversation = conversationRepository
                .findById(conversationId)
                .orElseThrow(() -> new RuntimeException("Conversation not found!"));

        // Save user message
        XAiMessage userMsg = new XAiMessage();
        userMsg.setConversation(conversation);
        userMsg.setRole(MessageRole.USER);
        userMsg.setContent(userMessage);
        userMsg.setCreatedAt(LocalDateTime.now());
        messageRepository.save(userMsg);

        // Get full message history for context
        List<XAiMessage> history =
                messageRepository.findByConversationIdOrderByCreatedAtAsc(
                        conversationId
                );

        // Call OpenAI with full conversation history
        String aiResponse = callOpenAi(history, userMessage);

        // Save AI response
        XAiMessage aiMsg = new XAiMessage();
        aiMsg.setConversation(conversation);
        aiMsg.setRole(MessageRole.ASSISTANT);
        aiMsg.setContent(aiResponse);
        aiMsg.setCreatedAt(LocalDateTime.now());
        messageRepository.save(aiMsg);

        return mapToDto(conversation, true);
    }

    // ─── Delete Conversation ──────────────────────────────

    public void deleteConversation(long conversationId) {
        XAiConversation conversation = conversationRepository
                .findById(conversationId)
                .orElseThrow(() -> new RuntimeException("Conversation not found!"));

        // Cascade delete removes all messages
        conversationRepository.delete(conversation);
    }

    // ─── OpenAI API Call ──────────────────────────────────

    private String callOpenAi(
            List<XAiMessage> history,
            String latestMessage
    ) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(openAiApiKey);

        // Build messages array — system prompt + conversation history
        List<Map<String, String>> messages = new ArrayList<>();

        // System prompt first
        messages.add(Map.of(
                "role", "system",
                "content", SYSTEM_PROMPT
        ));

        // Add conversation history
        for (XAiMessage msg : history) {
            messages.add(Map.of(
                    "role", msg.getRole() == MessageRole.USER ? "user" : "assistant",
                    "content", msg.getContent()
            ));
        }

        // Request body
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "llama-3.3-70b-versatile");
        requestBody.put("messages", messages);
        requestBody.put("max_tokens", 1000);
        requestBody.put("temperature", 0.7);

        HttpEntity<Map<String, Object>> entity =
                new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(
                    OPENAI_URL, entity, Map.class
            );

            if (response.getBody() != null) {
                List<Map<String, Object>> choices =
                        (List<Map<String, Object>>) response.getBody().get("choices");

                if (choices != null && !choices.isEmpty()) {
                    Map<String, Object> choice = choices.get(0);
                    Map<String, String> messageObj =
                            (Map<String, String>) choice.get("message");
                    return messageObj.get("content");
                }
            }

            return "I'm sorry, I couldn't process your request. Please try again.";

        } catch (Exception e) {
            return "I'm currently unavailable. Please try again in a moment.";
        }
    }

    // ─── Mapper ───────────────────────────────────────────

    private XAiConversationResponseDto mapToDto(
            XAiConversation conversation,
            boolean includeMessages
    ) {
        XAiConversationResponseDto dto = new XAiConversationResponseDto();
        dto.setId(conversation.getId());
        dto.setTitle(conversation.getTitle());
        dto.setCreatedAt(conversation.getCreatedAt());

        if (includeMessages) {
            List<XAiMessage> messages =
                    messageRepository.findByConversationIdOrderByCreatedAtAsc(
                            conversation.getId()
                    );

            List<XAiMessageDto> messageDtos = messages.stream()
                    .map(m -> {
                        XAiMessageDto msgDto = new XAiMessageDto();
                        msgDto.setId(m.getId());
                        msgDto.setRole(m.getRole());
                        msgDto.setContent(m.getContent());
                        msgDto.setCreatedAt(m.getCreatedAt());
                        return msgDto;
                    })
                    .collect(Collectors.toList());

            dto.setMessages(messageDtos);
        }

        return dto;
    }

    private User getUser(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found!"));
    }
}