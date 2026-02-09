package com.server.hackathon.model.controller.dto;

import java.util.List;

public record ModelRequestDto(
        String name,
        String imageName,
        String imageExtension,
        String summary,
        List<UsageDto> usage,
        List<TheoryDto> theory,
        List<ModelPartRequestDto> parts
) {}
