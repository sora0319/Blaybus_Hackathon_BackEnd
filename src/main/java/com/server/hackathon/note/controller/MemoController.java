package com.server.hackathon.note.controller;

import com.server.hackathon.common.ResponseDto;
import com.server.hackathon.note.controller.dto.MemoContentResponseDto;
import com.server.hackathon.note.controller.dto.MemoRequestDto;
import com.server.hackathon.note.controller.dto.MemoResponseDto;
import com.server.hackathon.note.service.MemoService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/models/{modelUuid}/memo")
@RequiredArgsConstructor
public class MemoController {

    private final MemoService memoService;

    @PutMapping
    public ResponseDto<MemoResponseDto> upsertMemo(
            @PathVariable String modelUuid,
            @RequestBody MemoRequestDto request,
            @AuthenticationPrincipal String memberUuid
    ) {
        MemoResponseDto response = memoService.upsertMemo(memberUuid, modelUuid, request);
        return ResponseDto.success("메모 저장 성공", response);
    }

    @GetMapping
    public ResponseDto<MemoContentResponseDto> getMemo(
            @PathVariable String modelUuid,
            @AuthenticationPrincipal String memberUuid
    ) {
        MemoContentResponseDto response = memoService.getMemo(memberUuid, modelUuid);
        return ResponseDto.success("메모 조회 성공", response);
    }
}
