package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ChatMessageDTO {
    private Integer message_id;
    private int room_id;
    private int sender_id;
    private String content;
    private LocalDateTime created_at;
    private Integer is_read;
}
