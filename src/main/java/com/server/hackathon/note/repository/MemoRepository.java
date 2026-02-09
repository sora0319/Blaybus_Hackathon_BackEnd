package com.server.hackathon.note.repository;

import com.server.hackathon.note.model.Memo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface MemoRepository extends JpaRepository<Memo, Long> {
    Optional<Memo> findByModelInstanceId(Long modelInstanceId);
}
