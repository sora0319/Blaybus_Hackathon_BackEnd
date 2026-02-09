package com.server.hackathon.model.controller.dto;

public record ModelsResponseDto(
        String modelUuid,
        String name,
        String imageUrl
) {
}
