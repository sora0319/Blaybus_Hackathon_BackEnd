package com.server.hackathon.model.repository;

import com.server.hackathon.model.model.ModelInstance;
import java.util.List;
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
}
