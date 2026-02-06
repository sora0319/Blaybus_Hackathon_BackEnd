package com.server.hackathon.common;

import com.server.hackathon.common.ResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import software.amazon.awssdk.services.s3.model.S3Exception;

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

    // s3 관련 에러
    @ExceptionHandler(S3Exception.class)
    public ResponseEntity<ResponseDto<Void>> handleS3Exception(S3Exception e) {
        log.error("S3 AWS Error: {}", e.awsErrorDetails().errorMessage());
        return ResponseEntity
                .status(e.statusCode()) // AWS가 준 상태 코드 그대로 사용
                .body(ResponseDto.fail("S3 연동 중 오류가 발생했습니다: " + e.awsErrorDetails().errorMessage()));
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