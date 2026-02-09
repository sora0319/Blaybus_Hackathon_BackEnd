package com.server.hackathon.model.model.vo;

public record PartState(
        String partUuid,
        Vector3 position,
        Vector3 rotation,
        Boolean isExploded
) {}
