package com.server.hackathon.model.controller.dto.response;

import com.server.hackathon.model.controller.dto.vo.ModelPartDto;
import com.server.hackathon.model.controller.dto.vo.TheoryDto;
import com.server.hackathon.model.controller.dto.vo.UsageDto;
import java.util.List;

public record ModelDetailResponse(
        String modelUuid,
        String title,
        String summary,
        List<UsageDto> usage,
        List<TheoryDto> theory,
        List<ModelPartDto> parts
) {}
