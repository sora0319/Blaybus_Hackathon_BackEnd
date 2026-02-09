package com.server.hackathon.model.repository;

import com.server.hackathon.model.model.Model;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ModelRepository extends JpaRepository<Model, Long> {

}
