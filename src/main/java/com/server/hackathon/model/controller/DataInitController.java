package com.server.hackathon.model.controller;

import com.server.hackathon.common.exception.CustomException;
import com.server.hackathon.member.client.model.Member;
import com.server.hackathon.member.client.repository.MemberRepository;
import com.server.hackathon.model.controller.dto.ModelPartRequestDto;
import com.server.hackathon.model.controller.dto.ModelRequestDto;
import com.server.hackathon.model.model.Model;
import com.server.hackathon.model.model.ModelInstance;
import com.server.hackathon.model.model.ModelPart;
import com.server.hackathon.model.repository.ModelInstanceRepository;
import com.server.hackathon.model.repository.ModelRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DataInitController {

    private final ModelRepository modelRepository;
    private final ModelInstanceRepository modelInstanceRepository;
    private final MemberRepository memberRepository;

    // 모델 데이터, 파츠 데이터 삽입
    @PostMapping("/models")
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

    @PostMapping("/instances/{userUuid}")
    @Transactional
    public ResponseEntity<String> initializeInstancesForUser(@PathVariable String userUuid) {

        // 사용자 조회 (UUID로 조회, 없으면 예외 발생)
        Member member = memberRepository.findByShortUuid(userUuid)
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, "해당 UUID를 가진 사용자가 없습니다: " + userUuid));

        // 시스템에 존재하는 모든 모델 조회
        List<Model> allModels = modelRepository.findAll();

        if (allModels.isEmpty()) {
            return ResponseEntity.ok("등록된 모델이 없어 인스턴스를 생성하지 않았습니다.");
        }

        // 모델 인스턴스 생성 (Simulation은 null)
        List<ModelInstance> newInstances = allModels.stream()
                .map(model -> ModelInstance.builder()
                        .member(member)
                        .model(model)
                        .build())
                .collect(Collectors.toList());

        // DB 저장
        modelInstanceRepository.saveAll(newInstances);

        return ResponseEntity.ok(String.format(
                "사용자(%s)에게 총 %d개의 모델 인스턴스가 성공적으로 지급되었습니다.",
                member.getShortUuid(),
                newInstances.size()
        ));
    }

}
