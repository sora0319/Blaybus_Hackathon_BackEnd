package com.server.hackathon.model.model;

import com.server.hackathon.ai.model.ChatRoom;
import com.server.hackathon.common.BaseEntity;
import com.server.hackathon.member.client.model.Member;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "model_instance")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ModelInstance extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    // N:1 관계 (어떤 모델을 기반으로 했는지)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "model_id")
    private Model model;

    @OneToOne(mappedBy = "modelInstance", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Simulation simulation;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "chatroom_id")
    private ChatRoom chatRoom;

    @Builder
    public ModelInstance(Member member, Model model) {
        this.member = member;
        this.model = model;
        this.chatRoom = new ChatRoom();
    }

    public void addSimulation(Simulation simulation) {
        this.simulation = simulation;
        simulation.setModelInstance(this);
    }


}
