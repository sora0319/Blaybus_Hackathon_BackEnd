package com.server.hackathon.model.service;

import com.server.hackathon.model.controller.dto.ModelsResponseDto;
import com.server.hackathon.model.model.Model;
import com.server.hackathon.model.model.ModelInstance;
import com.server.hackathon.model.repository.ModelInstanceRepository;
// import com.server.hackathon.common.service.ModelImageService; // 가정: 기존 이미지 URL 서비스

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ModelService {

    private final ModelInstanceRepository modelInstanceRepository;
    private final S3Service s3Service;

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

                    return new ModelsResponseDto(model.getId(), model.getName(), imageUrl);
                })
                .collect(Collectors.toList());
    }
}
