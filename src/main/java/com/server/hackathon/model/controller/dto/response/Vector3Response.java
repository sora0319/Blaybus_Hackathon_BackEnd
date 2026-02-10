package com.server.hackathon.model.controller.dto.response;

import com.server.hackathon.model.model.vo.Vector3;

public record Vector3Response(Double x, Double y, Double z) {
    public static Vector3Response from(Vector3 vo) {
        if (vo == null) return null;
        return new Vector3Response(vo.x(), vo.y(), vo.z());
    }
}
