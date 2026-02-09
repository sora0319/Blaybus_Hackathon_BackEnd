package com.server.hackathon.member.client.controller;


import com.server.hackathon.common.ResponseDto;
import com.server.hackathon.member.client.controller.dto.UserJoinRequest;
import com.server.hackathon.member.client.controller.dto.UserLoginRequest;
import com.server.hackathon.member.client.service.MemberService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    // [회원가입]
    @PostMapping("/signup")
    public ResponseDto<String> signUp(@RequestBody UserJoinRequest dto) {
        // 서비스에서 생성된 UUID를 받아옴
        String shortUuid = memberService.signUp(dto);

        // ResponseDto에 담아서 반환
        return ResponseDto.success("회원가입에 성공하였습니다.", shortUuid);
    }

    // [로그인]
    @PostMapping("/login")
    public ResponseDto<?> login(@RequestBody UserLoginRequest dto, HttpServletResponse response) {
        String token = memberService.login(dto);

        // 쿠키 생성 및 설정
        ResponseCookie cookie = ResponseCookie.from("AccessToken", token)
                .path("/")
                .sameSite("None")  // 중요: 서로 다른 도메인에서도 쿠키 전송 허용
                .secure(true)      // 중요: SameSite=None은 무조건 Secure=true여야 함
                .httpOnly(true)
                .maxAge(60 * 60 * 24)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        // ResponseDto 반환 (데이터는 null로 보냄)
        return ResponseDto.success("로그인 되었습니다.");
    }

    // [로그아웃]
    @PostMapping("/logout")
    public ResponseDto<?> logout(HttpServletResponse response) {
        // 쿠키 만료 처리
        Cookie cookie = new Cookie("AccessToken", null);
        cookie.setPath("/");
        cookie.setMaxAge(0); // 즉시 삭제

        response.addCookie(cookie);

        return ResponseDto.success("로그아웃 되었습니다.");
    }
}
