package com.server.hackathon.model.controller.dto;

public record ModelPartRequestDto(
        String name,
        String extension,
        String description
) {}
