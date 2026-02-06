package com.server.hackathon.member.auth;

import java.util.Collection;
import java.util.Objects; // 필수
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

@Getter
public class CustomUserDetails extends User {

    private final String shortUuid;

    public CustomUserDetails(String username, String password, Collection<? extends GrantedAuthority> authorities, String shortUuid) {
        super(username, password, authorities);
        // 여기서 null 체크를 하여 이후 사용 시 경고 발생을 막음
        this.shortUuid = shortUuid;
    }
}