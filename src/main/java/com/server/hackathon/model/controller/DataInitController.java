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
import com.server.hackathon.model.service.DataInitService;
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

    private final DataInitService dataInitService;

    @PostMapping("/modelsInfo")
    public String createRobotGripper(@RequestBody ModelRequestDto dto) {
        String modelName = dataInitService.createModel(dto);
        return "저장 완료! (model: " + modelName + ")";
    }

    @PostMapping("/instances/{userUuid}")
    public ResponseEntity<String> initializeInstances(@PathVariable String userUuid) {
        int count = dataInitService.initializeInstancesForUser(userUuid);

        if (count == 0) {
            return ResponseEntity.ok("등록된 모델이 없어 인스턴스를 생성하지 않았습니다.");
        }

        return ResponseEntity.ok(String.format(
                "사용자(%s)에게 총 %d개의 모델 인스턴스가 성공적으로 지급되었습니다.",
                userUuid,
                count
        ));
    }

}
