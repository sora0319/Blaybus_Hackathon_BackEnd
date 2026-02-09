package com.server.hackathon.model.controller.dto.request;

public record ModelPartRequestDto(
        String name,
        String fileName,
        String extension,
        String imageName,
        String imageExtension,
        String material,
        String description
) {}
