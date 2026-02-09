package com.server.hackathon.model.service;

import com.server.hackathon.common.exception.CustomException;
import com.server.hackathon.model.controller.dto.request.SimulationSaveRequest;
import com.server.hackathon.model.model.ModelInstance;
import com.server.hackathon.model.model.Simulation;
import com.server.hackathon.model.repository.ModelInstanceRepository;
import com.server.hackathon.model.repository.SimulationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class SimulationService {

    private final ModelInstanceRepository modelInstanceRepository;
    private final SimulationRepository simulationRepository;

    public String saveSimulation(String memberUuid, String modelUuid, SimulationSaveRequest request) {
        ModelInstance modelInstance = modelInstanceRepository.findByMemberShortUuidAndModelShortUuid(memberUuid,modelUuid)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "해당 모델에 대한 사용자의 인스턴스를 찾을 수 없습니다."));

        Simulation simulation = modelInstance.getSimulation();

        if(simulation==null){
            simulation = Simulation.builder()
                    .modelInstance(modelInstance)
                    .build();
            modelInstance.addSimulation(simulation);
            simulationRepository.save(simulation);
        }

        simulation.updateState(request.assembly(), request.simulator(), request.edit());

        return simulation.getShortUuid();
    }
}
