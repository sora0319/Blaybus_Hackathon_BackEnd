package com.server.hackathon.model.controller.dto.response;

import com.server.hackathon.model.model.vo.Quaternion;

public record QuaternionResponse(Double x, Double y, Double z, Double w) {
    public static QuaternionResponse from(Quaternion vo) {
        if (vo == null) return null;
        return new QuaternionResponse(vo.x(), vo.y(), vo.z(), vo.w());
    }
}
