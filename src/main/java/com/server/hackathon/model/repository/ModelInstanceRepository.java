package com.server.hackathon.model.repository;

import com.server.hackathon.model.model.ModelInstance;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ModelInstanceRepository extends JpaRepository<ModelInstance, Long> {
    <S extends ModelInstance> List<S> saveAll(Iterable<S> entities);


    /**
     * 성능 최적화를 위해 Model 엔티티 Fetch Join
     */
    @Query("SELECT mi FROM ModelInstance mi " +
            "JOIN FETCH mi.model " +
            "WHERE mi.member.shortUuid = :memberUuid")
    List<ModelInstance> findAllByMemberShortUuid(@Param("memberUuid") String memberUuid);


    @Query("SELECT mi FROM ModelInstance mi " +
            "JOIN FETCH mi.model " +
            "JOIN FETCH mi.member " +
            "WHERE mi.member.shortUuid = :memberShortUuid " +
            "AND mi.model.shortUuid = :modelShortUuid")
    Optional<ModelInstance> findByMemberShortUuidAndModelShortUuid(
            @Param("memberShortUuid") String memberShortUuid,
            @Param("modelShortUuid") String modelShortUuid
    );

    Optional<ModelInstance> findByShortUuid(String modelUuid);
}
