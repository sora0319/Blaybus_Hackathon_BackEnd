package com.server.hackathon.member.client.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.UUID;
import java.util.Base64;

@Entity
@Getter
@Table(name = "users")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true, length = 25)
    private String shortUuid;

    // 엔티티가 UUID를 생성하고 짧게 변환
    @PrePersist
    public void createShortUuid() {
        UUID uuid = UUID.randomUUID();
        // UUID의 byte 배열을 Base64로 인코딩하여 길이를 단축 (약 22자)
        this.shortUuid = Base64.getUrlEncoder().withoutPadding().encodeToString(asBytes(uuid));
    }

    private static byte[] asBytes(UUID uuid) {
        java.nio.ByteBuffer bb = java.nio.ByteBuffer.wrap(new byte[16]);
        bb.putLong(uuid.getMostSignificantBits());
        bb.putLong(uuid.getLeastSignificantBits());
        return bb.array();
    }

    @Builder
    private Users(String username, String password, String email){
        this.username = username;
        this.password = password;
        this.email = email;
    }
}