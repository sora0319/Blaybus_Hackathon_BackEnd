package com.server.hackathon.model.controller.dto.request;

import com.server.hackathon.model.model.vo.AssemblyData;
import com.server.hackathon.model.model.vo.SimulatorData;

public record SimulationSaveRequest(
        AssemblyData assembly,  // 조립 탭 데이터 (카메라만 있음)
        SimulatorData edit,     // 편집 탭 데이터 (카메라 + 부품)
        SimulatorData simulator // 시뮬레이터 탭 데이터 (카메라 + 부품)
) {}
