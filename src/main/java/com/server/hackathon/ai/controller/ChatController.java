package com.server.hackathon.ai.controller;

import com.server.hackathon.ai.controller.dto.response.ChatMessageResponse;
import com.server.hackathon.ai.service.ChatService;
import com.server.hackathon.common.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    //private final ReportService reportService;

    // 메시지 전송 (RAG + 저장)
    @PostMapping("/{modelUuid}/message")
    public ResponseDto<String> sendMessage(
            @AuthenticationPrincipal String memberUuid,
            @PathVariable String modelUuid,
            @RequestBody String message) {
        String response = chatService.processMessage(memberUuid, modelUuid,message);
        return ResponseDto.success(response);
    }

    // 이전 대화 내역 조회 (화면 복구용)
    @GetMapping("/{modelUuid}/history")
    public ResponseDto<List<ChatMessageResponse>> getHistory(
            @AuthenticationPrincipal String memberUuid,
            @PathVariable String modelUuid) {
        List<ChatMessageResponse> history = chatService.getChatHistory(memberUuid, modelUuid);

        return ResponseDto.success("사용자 대화 내역", history);
    }


}
