package com.server.hackathon.note.model;

import com.server.hackathon.common.BaseEntity;
import com.server.hackathon.model.model.ModelInstance;
import com.server.hackathon.note.model.vo.MemoContent;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Getter
@Table(name = "memo")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Memo extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "json")
    private MemoContent content;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "model_instance_id", nullable = false, unique = true)
    private ModelInstance modelInstance;

    @Builder
    public Memo(MemoContent content, ModelInstance modelInstance) {
        this.content = content;
        this.modelInstance = modelInstance;
    }

    public void updateContent(MemoContent content) {
        this.content = content;
    }
}
