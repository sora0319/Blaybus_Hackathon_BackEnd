package com.server.hackathon.model.repository;

import com.server.hackathon.model.model.Simulation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SimulationRepository extends JpaRepository<Simulation, Long> {

}
