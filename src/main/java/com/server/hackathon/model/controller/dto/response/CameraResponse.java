package com.server.hackathon.model.controller.dto.response;

import com.server.hackathon.model.model.vo.CameraState;

public record CameraResponse(
        Vector3Response position,
        Vector3Response target,
        Vector3Response rotation,
        QuaternionResponse quaternion,
        Double zoom,
        Integer fov
) {
    public static CameraResponse from(CameraState vo) {
        if (vo == null) return null;
        return new CameraResponse(
                Vector3Response.from(vo.position()),
                Vector3Response.from(vo.target()),
                Vector3Response.from(vo.rotation()),
                QuaternionResponse.from(vo.quaternion()),
                vo.zoom(),
                vo.fov()
        );
    }
}
