package com.server.hackathon.model.controller.dto.response;

import com.server.hackathon.model.model.vo.SimulatorData;
import java.util.List;
import java.util.stream.Collectors;

public record SimulatorResponse(
        CameraResponse cameraState,
        List<PartResponse> partsState
) {
    public static SimulatorResponse from(SimulatorData vo) {
        if (vo == null) return null;
        return new SimulatorResponse(
                CameraResponse.from(vo.cameraState()),
                (vo.partsState() != null) ?
                        vo.partsState().stream().map(PartResponse::from).toList()
                        : null
        );
    }
}
