package com.server.hackathon.model.controller;

import com.server.hackathon.common.ResponseDto;
import com.server.hackathon.model.controller.dto.request.SimulationSaveRequest;
import com.server.hackathon.model.service.SimulationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/models/{modelUuid}/simulations")
@RequiredArgsConstructor
public class SimulationController {

    private final SimulationService simulationService;

    @PostMapping
    public ResponseDto<String> saveSimulation(
            @AuthenticationPrincipal String memberUuid,
            @PathVariable String modelUuid,
            @RequestBody SimulationSaveRequest request
    ) {
        if (request == null) {
            return ResponseDto.fail("요청 데이터가 비어있습니다.");
        }

        String simulationUuid = simulationService.saveSimulation(memberUuid, modelUuid, request);
        return ResponseDto.success("사용자 시뮬레이션 데이터 저장 성공", simulationUuid);
    }
}
