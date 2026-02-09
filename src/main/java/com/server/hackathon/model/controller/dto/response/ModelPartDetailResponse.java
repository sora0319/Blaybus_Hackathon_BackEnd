package com.server.hackathon.model.controller.dto.response;

public record ModelPartDetailResponse(
        String partUuid,
        String name,
        String material,
        String description,
        String partModelUrl,
        String thumbnailUrl
) {}
