package com.server.hackathon.ai.model;

import com.server.hackathon.ai.model.vo.MessageRole;
import com.server.hackathon.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Getter
@NoArgsConstructor
public class ChatMessage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private MessageRole role; // USER, ASSISTANT

    @Lob
    @Column(name = "content", columnDefinition = "LONGTEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private ChatRoom chatRoom;

    @Builder
    public ChatMessage(MessageRole role, String content, ChatRoom chatRoom) {
        this.role = role;
        this.content = content;
        this.chatRoom = chatRoom;
    }


}
