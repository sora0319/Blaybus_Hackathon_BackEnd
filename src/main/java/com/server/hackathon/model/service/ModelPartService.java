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
public class ModelPartService {

    private final ModelPartRepository modelPartRepository;
    private final S3Service s3Service;

    @Transactional(readOnly = true)
    public ModelPartDetailResponse getPartDetail(String memberUuid, String partUuid) {
        ModelPart part = modelPartRepository.findPartByUuidAndMember(partUuid, memberUuid)
                .orElseThrow(() -> new CustomException(HttpStatus.FORBIDDEN, "부품을 찾을 수 없거나 접근 권한이 없습니다."));

        // 3d Model URL 생성
        String partModelUrl = s3Service.getPresignedGetUrl(
                part.getModel().getName(),
                part.getFileName(),
                part.getExtension()
        );

        // thumbnail URL 생성
        String thumbnailUrl = s3Service.getPresignedGetUrl(
                part.getModel().getName(),
                part.getImageName(),
                part.getImageExtension()
        );

        return new ModelPartDetailResponse(
                part.getShortUuid(),
                part.getName(),
                part.getMaterial(),
                part.getDescription(),
                partModelUrl,
                thumbnailUrl
        );
    }
}
