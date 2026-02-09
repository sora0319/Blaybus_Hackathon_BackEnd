package com.server.hackathon.model.controller.dto.response;

public record ModelsResponseDto(
        String modelUuid,
        String name,
        String imageUrl
) {
}
