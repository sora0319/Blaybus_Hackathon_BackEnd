package com.server.hackathon.ai.controller;

import com.server.hackathon.ai.service.DataInjectionService;
import com.server.hackathon.common.ResponseDto;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/vector/injection")
@RequiredArgsConstructor
public class AiController {

    private final DataInjectionService  dataInjectionService;

    @GetMapping
    public ResponseDto<String> savedVector() {
        System.out.println("MySQL -> Pinecone 데이터 이관");
        dataInjectionService.ingestData();
        
        return ResponseDto.success("이관 완료");
    }
}
