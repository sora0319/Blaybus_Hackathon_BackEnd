package com.server.hackathon.member.client.service;

import com.server.hackathon.member.auth.JwtTokenProvider;
import com.server.hackathon.member.client.controller.dto.UserJoinRequest;
import com.server.hackathon.member.client.controller.dto.UserLoginRequest;
import com.server.hackathon.member.client.model.Users;
import com.server.hackathon.member.client.repository.UserRepository;
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

    // [회원가입]
    @Transactional
    public String signUp(UserJoinRequest dto) {
        if (userRepository.existsByUsername((dto.username()))) {
            throw new IllegalArgumentException("이미 사용 중인 아이디입니다.");
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
        Users user = userRepository.findByUsername(dto.username())
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 아이디입니다."));

        if (!passwordEncoder.matches(dto.password(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // 토큰 생성 및 반환
        return jwtTokenProvider.createToken(user.getShortUuid());
    }
}
