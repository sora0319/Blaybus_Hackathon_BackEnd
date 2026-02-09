package com.server.hackathon.model.controller.dto.request;

import com.server.hackathon.model.controller.dto.vo.TheoryDto;
import com.server.hackathon.model.controller.dto.vo.UsageDto;
import java.util.List;

public record ModelRequestDto(
        String name,
        String imageName,
        String imageExtension,
        String summary,
        List<UsageDto> usage,
        List<TheoryDto> theory,
        List<ModelPartRequestDto> parts
) {}
