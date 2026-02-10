package com.server.hackathon.model.controller.dto.response;

import com.server.hackathon.model.model.vo.PartState;

public record PartResponse(
        String partUuid,
        Vector3Response position,
        Vector3Response rotation,
        Boolean isExploded
) {
    public static PartResponse from(PartState vo) {
        if (vo == null) return null;
        return new PartResponse(
                vo.partUuid(),
                Vector3Response.from(vo.position()),
                Vector3Response.from(vo.rotation()),
                vo.isExploded()
        );
    }
}
