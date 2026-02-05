package com.server.hackathon.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResponseDto<T> {
    private boolean success; // 성공 여부 (true/false)
    private String message;  // 응답 메시지
    private T data;          // 실제 데이터 (없으면 null)

    // 성공 시 데이터를 반환하는 정적 팩토리 메서드
    public static <T> ResponseDto<T> success(String message, T data) {
        return new ResponseDto<>(true, message, data);
    }

    // 데이터 없이 메시지만 반환하는 경우
    public static <T> ResponseDto<T> success(String message) {
        return new ResponseDto<>(true, message, null);
    }

    // 실패 시 사용하는 메서드 (예외 처리 시 활용)
    public static <T> ResponseDto<T> fail(String message) {
        return new ResponseDto<>(false, message, null);
    }
}
