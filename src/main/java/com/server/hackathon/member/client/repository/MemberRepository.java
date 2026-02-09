package com.server.hackathon.member.client.repository;

import com.server.hackathon.member.client.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUsername(String username);

    Optional<Member> findByEmail(String email);

    Optional<Member> findByShortUuid(String shortUuid);


    boolean existsByUsername(String username);
}
