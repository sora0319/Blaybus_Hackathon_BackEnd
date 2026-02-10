package com.server.hackathon.model.controller.dto.response;

import com.server.hackathon.model.model.vo.AssemblyData;

public record AssemblyResponse(
        CameraResponse cameraState
) {
    public static AssemblyResponse from(AssemblyData vo) {
        if (vo == null) return null;
        return new AssemblyResponse(CameraResponse.from(vo.cameraState()));
    }
}
