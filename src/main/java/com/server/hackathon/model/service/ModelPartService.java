package com.server.hackathon.model.service;

import com.server.hackathon.common.exception.CustomException;
import com.server.hackathon.model.controller.dto.response.ModelPartDetailResponse;
import com.server.hackathon.model.model.ModelPart;
import com.server.hackathon.model.repository.ModelPartRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ModelPartService {

    private final ModelPartRepository modelPartRepository;
    private final S3Service s3Service;

    public ModelPartDetailResponse getPartDetail(String memberUuid, String partUuid) {
        ModelPart part = modelPartRepository.findPartByUuidAndMember(partUuid, memberUuid)
                .orElseThrow(() -> new CustomException(HttpStatus.FORBIDDEN, "부품을 찾을 수 없거나 접근 권한이 없습니다."));

        // URL 생성
        String partUrl = s3Service.getPresignedGetUrl(
                part.getModel().getName(),
                part.getName(),
                part.getExtension()
        );

        return new ModelPartDetailResponse(
                part.getShortUuid(),
                partUrl,
                part.getDescription()
        );
    }
}
