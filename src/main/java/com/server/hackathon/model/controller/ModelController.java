package com.server.hackathon.model.controller;

import com.server.hackathon.common.ResponseDto;
import com.server.hackathon.model.controller.dto.ModelDetailResponse;
import com.server.hackathon.model.controller.dto.ModelsResponseDto;
import com.server.hackathon.model.service.ModelService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/3d/models")
@RequiredArgsConstructor
public class ModelController {

    private final ModelService modelService;

    @GetMapping
    public ResponseDto<List<ModelsResponseDto>> getMyModels(
            @AuthenticationPrincipal String memberUuid
    ) {
        // 서비스 호출
        List<ModelsResponseDto> models = modelService.getMyModels(memberUuid);

        // 공통 ResponseDto로 감싸서 반환
        return ResponseDto.success("사용자 모델 목록 조회 성공", models);
    }

    @GetMapping("/{modelUuid}")
    public ResponseDto<ModelDetailResponse> getModelDetail(
            @AuthenticationPrincipal String memberUuid,
            @PathVariable String modelUuid
    ) {
        ModelDetailResponse response = modelService.getModelDetail(memberUuid, modelUuid);

        return ResponseDto.success("모델 상세 정보 조회 성공", response);
    }
}
