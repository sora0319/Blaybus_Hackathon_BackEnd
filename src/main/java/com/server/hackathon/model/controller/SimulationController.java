package com.server.hackathon.model.controller;

import com.server.hackathon.common.ResponseDto;
import com.server.hackathon.model.controller.dto.request.SimulationSaveRequest;
import com.server.hackathon.model.controller.dto.response.AssemblyResponse;
import com.server.hackathon.model.controller.dto.response.SimulatorResponse;
import com.server.hackathon.model.model.vo.AssemblyData;
import com.server.hackathon.model.service.SimulationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping("/assembly")
    public ResponseDto<AssemblyResponse> getAssembly(
            @AuthenticationPrincipal String memberUuid,
            @PathVariable String modelUuid
    ) {
        AssemblyResponse response = simulationService.getAssemblyData(memberUuid, modelUuid);
        return ResponseDto.success("Assembly 데이터 조회 성공", response);
    }

    @GetMapping("/simulator")
    public ResponseDto<SimulatorResponse> getSimulator(
            @AuthenticationPrincipal String memberUuid,
            @PathVariable String modelUuid
    ) {
        SimulatorResponse response = simulationService.getSimulatorData(memberUuid, modelUuid);
        return ResponseDto.success("Simulator 데이터 조회 성공", response);
    }

    @GetMapping("/edit")
    public ResponseDto<SimulatorResponse> getEdit(
            @AuthenticationPrincipal String memberUuid,
            @PathVariable String modelUuid
    ) {
        SimulatorResponse response = simulationService.getEditData(memberUuid, modelUuid);
        return ResponseDto.success("Edit 데이터 조회 성공", response);
    }
}
