package com.server.hackathon.common;

import com.server.hackathon.common.ResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice // 모든 컨트롤러에서 발생하는 예외를 여기서 잡습니다.
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ResponseDto<Void>> handleCustomException(CustomException e) {
        log.warn("CustomException Occurred: {}", e.getMessage());
        return ResponseEntity
                .status(e.getStatus())
                .body(ResponseDto.fail(e.getMessage()));
    }

    // 그외 오류
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDto<Void>> handleGeneralException(Exception e) {
        log.error("Unhandled Exception: ", e); // 스택 트레이스 로그 남기기
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ResponseDto.fail("서버 내부 오류가 발생했습니다. 관리자에게 문의하세요."));
    }
}