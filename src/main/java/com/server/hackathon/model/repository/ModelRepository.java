package com.server.hackathon.model.repository;

import com.server.hackathon.model.model.Model;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ModelRepository extends JpaRepository<Model, Long> {
    List<Model> findAll();

    /**
     * 모델의 shortUuid와 사용자의 식별자(memberUuid)를 이용해 조회.
     * ModelPart를 Fetch Join 하여 N+1 문제 해결
     * ModelInstance와 JOIN 하여 사용자가 소유한 모델인지 검증
     */
    @Query("SELECT DISTINCT m FROM Model m " +
            "JOIN FETCH m.modelParts " +
            "JOIN ModelInstance mi ON mi.model = m " +
            "WHERE m.shortUuid = :modelUuid " +
            "AND mi.member.shortUuid = :memberUuid")
    Optional<Model> findModelWithPartsByUuidAndMember(
            @Param("modelUuid") String modelUuid,
            @Param("memberUuid") String memberUuid
    );
}
