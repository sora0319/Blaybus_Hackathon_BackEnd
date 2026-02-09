package com.server.hackathon.model.model;

import com.server.hackathon.common.BaseEntity;
import com.server.hackathon.model.model.vo.AssemblyData;
import com.server.hackathon.model.model.vo.SimulatorData;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Getter
@Table(name = "simulation")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Simulation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "assembly", columnDefinition = "json")
    private AssemblyData assembly;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "simulator", columnDefinition = "json")
    private SimulatorData simulator;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "edit", columnDefinition = "json")
    private SimulatorData edit;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "model_instance_id", unique = true)
    private ModelInstance modelInstance;

    @Builder
    public Simulation(ModelInstance modelInstance) {
        this.modelInstance = modelInstance;
    }

    public void setModelInstance(ModelInstance modelInstance) {
        this.modelInstance = modelInstance;
    }

    // null 이 아닐때만 덮어쓰기
    public void updateState(AssemblyData assembly, SimulatorData simulator, SimulatorData edit) {
        if (assembly != null) {
            this.assembly = assembly;
        }
        if (simulator != null) {
            this.simulator = simulator;
        }
        if (edit != null) {
            this.edit = edit;
        }
    }

}
