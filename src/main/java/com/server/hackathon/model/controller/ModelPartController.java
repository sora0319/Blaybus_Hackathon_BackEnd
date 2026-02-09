package com.server.hackathon.model.controller;

import com.server.hackathon.common.ResponseDto;
import com.server.hackathon.model.controller.dto.ModelPartDetailResponse;
import com.server.hackathon.model.service.ModelPartService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/parts")
@RequiredArgsConstructor
public class ModelPartController {

    private final ModelPartService modelPartService;

    @GetMapping("/{partUuid}")
    public ResponseDto<ModelPartDetailResponse> getPartDetail(
            @AuthenticationPrincipal String memberUuid,
            @PathVariable String partUuid
    ) {
        ModelPartDetailResponse response = modelPartService.getPartDetail(memberUuid, partUuid);

        return ResponseDto.success("부품 상세 정보 조회 성공", response);
    }
}
