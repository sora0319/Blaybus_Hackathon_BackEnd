package com.server.hackathon.model.controller;

import com.server.hackathon.model.controller.dto.ModelPartRequestDto;
import com.server.hackathon.model.controller.dto.ModelRequestDto;
import com.server.hackathon.model.model.Model;
import com.server.hackathon.model.model.ModelPart;
import com.server.hackathon.model.repository.ModelRepository;
import lombok.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/models")
@RequiredArgsConstructor
public class DataInitController {

    private final ModelRepository modelRepository;

    // 모델 데이터, 파츠 데이터 삽입
    @PostMapping
    @Transactional
    public String createModelWithParts(@RequestBody ModelRequestDto request) {

        Model model = Model.builder()
                .name(request.name())
                .description(request.description())
                .imageName(request.imageName())
                .imageExtension(request.imageExtension())
                .build();

        if (request.parts() != null) {
            for (ModelPartRequestDto partDto : request.parts()) {
                ModelPart part = ModelPart.builder()
                        .name(partDto.name())
                        .extension(partDto.extension())
                        .description(partDto.description())
                        .build();

                // 양방향 설정
                model.addPart(part);
            }
        }

        modelRepository.save(model);

        return "저장 완료! (ID: " + model.getId() + ")";
    }
}
