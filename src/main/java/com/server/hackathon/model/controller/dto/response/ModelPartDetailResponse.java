package com.server.hackathon.model.controller.dto.response;

public record ModelPartDetailResponse(
        String partUuid,
        String partUrl,
        String description
) {}
