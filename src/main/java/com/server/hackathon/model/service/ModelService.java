package com.server.hackathon.model.service;

import com.server.hackathon.model.controller.dto.response.ModelDetailResponse;
import com.server.hackathon.model.controller.dto.vo.ModelPartDto;
import com.server.hackathon.model.controller.dto.response.ModelsResponseDto;
import com.server.hackathon.model.model.Model;
import com.server.hackathon.model.model.ModelInstance;
import com.server.hackathon.model.repository.ModelInstanceRepository;
// import com.server.hackathon.common.service.ModelImageService; // 가정: 기존 이미지 URL 서비스

import com.server.hackathon.model.repository.ModelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ModelService {

    private final ModelRepository modelRepository;
    private final ModelInstanceRepository modelInstanceRepository;
    private final S3Service s3Service;

    @Transactional(readOnly = true)
    public List<ModelsResponseDto> getMyModels(String memberUuid) {
        List<ModelInstance> instances = modelInstanceRepository.findAllByMemberShortUuid(memberUuid);

        return instances.stream()
                .map(instance -> {
                    Model model = instance.getModel();

                    // 기존 서비스 사용하여 URL 생성
                    String imageUrl = s3Service.getPresignedGetUrl(
                            model.getName(),
                            model.getImageName(),
                            model.getImageExtension()
                    );

                    return new ModelsResponseDto(model.getShortUuid(), model.getName(), imageUrl);
                })
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ModelDetailResponse getModelDetail(String memberUuid, String modelUuid) {
        // 부품 조회
        Model model = modelRepository.findModelWithPartsByUuidAndMember(modelUuid, memberUuid)
                .orElseThrow(() -> new IllegalArgumentException("모델을 찾을 수 없거나 접근 권한이 없습니다."));

        // 부품 리스트 DTO 변환
        List<ModelPartDto> partResponses = model.getModelParts().stream()
                .map(part -> {
                    String fileUrl = s3Service.getPresignedGetUrl(
                            model.getName(),
                            part.getName(),
                            part.getExtension()
                    );

                    return new ModelPartDto(
                            part.getShortUuid(),
                            fileUrl
                    );
                })
                .toList();

        return new ModelDetailResponse(
                model.getShortUuid(),
                model.getDescription(),
                partResponses
        );
    }
}
