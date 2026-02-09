package com.server.hackathon.model.controller.dto;

import java.util.List;

public record ModelDetailResponse(
        String modelUuid,
        String description,
        List<ModelPartResponse> parts
) {}
