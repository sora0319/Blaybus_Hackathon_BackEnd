package com.server.hackathon.ai.controller.dto.response;

import com.server.hackathon.ai.model.ChatMessage;
import com.server.hackathon.ai.model.vo.MessageRole;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ChatMessageResponse {

    private Long id;
    private MessageRole role;      // USER or ASSISTANT
    private String content;
    private LocalDateTime createdAt;

    public static ChatMessageResponse from(ChatMessage entity) {
        return ChatMessageResponse.builder()
                .id(entity.getId())
                .role(entity.getRole())
                .content(entity.getContent())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
