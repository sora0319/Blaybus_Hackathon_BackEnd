package com.server.hackathon.member.client.repository;

import com.server.hackathon.member.client.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByUsername(String username);

    Optional<Users> findByEmail(String email);

    Optional<Users> findByShortUuid(String shortUuid);


    boolean existsByUsername(String username);
}
