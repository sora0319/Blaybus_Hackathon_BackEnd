package com.server.hackathon.model.service;

import com.server.hackathon.common.exception.CustomException;
import com.server.hackathon.member.client.model.Member;
import com.server.hackathon.member.client.repository.MemberRepository;
import com.server.hackathon.model.controller.dto.request.ModelPartRequestDto;
import com.server.hackathon.model.controller.dto.request.ModelRequestDto;
import com.server.hackathon.model.controller.dto.vo.TheoryDto;
import com.server.hackathon.model.controller.dto.vo.UsageDto;
import com.server.hackathon.model.model.Model;
import com.server.hackathon.model.model.ModelInstance;
import com.server.hackathon.model.model.ModelPart;
import com.server.hackathon.model.model.ModelTheory;
import com.server.hackathon.model.model.ModelUsage;
import com.server.hackathon.model.repository.ModelInstanceRepository;
import com.server.hackathon.model.repository.ModelRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DataInitService {

    private final MemberRepository memberRepository;
    private final ModelRepository modelRepository;
    private final ModelInstanceRepository modelInstanceRepository;

    @Transactional
    public String createModel(ModelRequestDto dto) {

        Model model = Model.builder()
                .name(dto.name())
                .description(dto.summary())
                .imageName(dto.imageName())
                .imageExtension(dto.imageExtension())
                .build();

        if (dto.usage() != null) {
            for (UsageDto usageDto : dto.usage()) {
                ModelUsage usage = ModelUsage.builder()
                        .title(usageDto.title())
                        .content(usageDto.content())
                        .build();
                model.addUsage(usage);
            }
        }

        if (dto.theory() != null) {
            for (TheoryDto theoryDto : dto.theory()) {
                ModelTheory theory = ModelTheory.builder()
                        .title(theoryDto.title())
                        .content(theoryDto.content())
                        .details(theoryDto.details())
                        .build();
                model.addTheory(theory);
            }
        }

        // Parts 엔티티 저장
        if (dto.parts() != null) {
            for (ModelPartRequestDto partDto : dto.parts()) {
                ModelPart part = ModelPart.builder()
                        .name(partDto.name())
                        .fileName(partDto.fileName())
                        .extension(partDto.extension())
                        .imageName(partDto.imageName())
                        .imageExtension(partDto.imageExtension())
                        .material(partDto.material())
                        .description(partDto.description())
                        .build();
                model.addPart(part);
            }
        }

        modelRepository.save(model);

        return model.getName();
    }

    @Transactional
    public int initializeInstancesForUser(String userUuid) {
        Member member = memberRepository.findByShortUuid(userUuid)
                .orElseThrow(() -> new CustomException(
                        HttpStatus.BAD_REQUEST,
                        "해당 UUID를 가진 사용자가 없습니다: " + userUuid));

        List<Model> allModels = modelRepository.findAll();

        if (allModels.isEmpty()) {
            return 0;
        }

        List<ModelInstance> newInstances = allModels.stream()
                .map(model -> ModelInstance.builder()
                        .member(member)
                        .model(model)
                        .build())
                .collect(Collectors.toList());


        modelInstanceRepository.saveAll(newInstances);

        return newInstances.size();
    }
}

