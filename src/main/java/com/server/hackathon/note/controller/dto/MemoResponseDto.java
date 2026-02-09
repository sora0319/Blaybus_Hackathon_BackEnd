package com.server.hackathon.note.controller.dto;

public record MemoResponseDto(
        String memoUuid,
        String updatedAt
) {}