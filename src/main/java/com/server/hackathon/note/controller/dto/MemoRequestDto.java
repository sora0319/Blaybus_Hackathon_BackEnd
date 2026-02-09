package com.server.hackathon.note.controller.dto;

import com.server.hackathon.note.model.vo.MemoContent;

public record MemoRequestDto(
        MemoContent content
) {}
