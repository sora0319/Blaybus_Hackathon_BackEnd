package com.server.hackathon.model.controller.dto;

public record ModelPartDetailResponse(
        String partUuid,
        String partUrl,
        String description
) {}
