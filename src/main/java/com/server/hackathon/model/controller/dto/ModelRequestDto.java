package com.server.hackathon.model.controller.dto;

import java.util.List;

public record ModelRequestDto(
        String name,
        String description,
        String imageName,
        String imageExtension,
        List<ModelPartRequestDto> parts
) {}
