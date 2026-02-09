package com.server.hackathon.model.controller.dto;

public record ModelsResponseDto(
        Long modelId,
        String name,
        String imageUrl
) {
}
