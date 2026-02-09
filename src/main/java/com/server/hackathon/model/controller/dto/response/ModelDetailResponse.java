package com.server.hackathon.model.controller.dto.response;

import com.server.hackathon.model.controller.dto.vo.ModelPartDto;
import java.util.List;

public record ModelDetailResponse(
        String modelUuid,
        String description,
        List<ModelPartDto> parts
) {}
