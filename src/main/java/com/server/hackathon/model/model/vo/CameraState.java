package com.server.hackathon.model.model.vo;

public record CameraState(
        Vector3 position,
        Vector3 target,
        Vector3 rotation,
        Quaternion quaternion,
        Double zoom,
        Integer fov
) {}