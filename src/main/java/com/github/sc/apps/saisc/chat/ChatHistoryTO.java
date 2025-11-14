package com.github.sc.apps.saisc.chat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "v_chat_history")
public class ChatHistoryTO {
    @Id
    @Column(name = "conversation_id")
    String conversationId;
    @Column(name = "start_timestamp")
    LocalDateTime start;
    @Column(name = "end_timestamp")
    LocalDateTime end;
    @Column(name = "message_count")
    int messageCount;
}
