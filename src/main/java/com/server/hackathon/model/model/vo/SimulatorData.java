package com.server.hackathon.model.model.vo;

import java.util.List;

public record SimulatorData(
        CameraState cameraState,
        List<PartState> partsState
) {}