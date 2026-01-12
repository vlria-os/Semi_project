package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ChatListUpdateDto {
    private int roomId;
    private int unreadCount;
    private String lastMessage;
    private LocalDateTime lastTime;
    private Integer userCount;
    private String roomType;
    private String roomName;
    private String action; // "upsert" or "remove"
}
