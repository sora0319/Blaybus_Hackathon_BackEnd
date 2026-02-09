package com.server.hackathon.model.repository;

import com.server.hackathon.model.model.ModelPart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ModelPartRepository extends JpaRepository<ModelPart, Long> {

    /**
     * 부품의 UUID로 조회하되,
     * 해당 부품의 상위 모델을 사용자가 ModelInstance로 가지고 있는지 검증
     */
    @Query("SELECT mp FROM ModelPart mp " +
            "JOIN mp.model m " +
            "JOIN ModelInstance mi ON mi.model = m " +
            "WHERE mp.shortUuid = :partUuid " +
            "AND mi.member.shortUuid = :memberUuid")
    Optional<ModelPart> findPartByUuidAndMember(
            @Param("partUuid") String partUuid,
            @Param("memberUuid") String memberUuid
    );
}
