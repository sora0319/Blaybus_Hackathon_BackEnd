package com.server.hackathon.member.client.service;

import com.server.hackathon.common.CustomException;
import com.server.hackathon.member.auth.CustomUserDetails;
import com.server.hackathon.member.auth.JwtTokenProvider;
import com.server.hackathon.member.client.controller.dto.UserJoinRequest;
import com.server.hackathon.member.client.controller.dto.UserLoginRequest;
import com.server.hackathon.member.client.model.Users;
import com.server.hackathon.member.client.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    // [회원가입]
    @Transactional
    public String signUp(UserJoinRequest dto) {
        if (userRepository.existsByUsername((dto.username()))) {
            throw new CustomException(HttpStatus.BAD_REQUEST, "Username is already in use");
        }

        Users user = Users.builder()
                        .username(dto.username())
                        .password(passwordEncoder.encode(dto.password()))
                        .email(dto.email())
                        .build();

        // 저장 후 생성된 UUID 반환
        return userRepository.save(user).getShortUuid();
    }

    // [로그인]
    public String login(UserLoginRequest dto) {
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(dto.username(), dto.password());

        try {
            // 인증 관리자에게 검증 요청 (CustomUserDetailsService 호출)
            Authentication authentication = authenticationManager.authenticate(authToken);

            // 3. 인증 성공 시, Principal에서 정보 추출
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

            // 4. JWT 생성 및 반환
            return jwtTokenProvider.createToken(userDetails.getShortUuid());

        } catch (BadCredentialsException e) {
            // 5. 예외 변환: 시큐리티 예외 -> 커스텀 예외 (401 Unauthorized)
            throw new CustomException(HttpStatus.UNAUTHORIZED, "아이디 또는 비밀번호가 일치하지 않습니다.");
        }
    }
}
