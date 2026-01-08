package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ChatRoomDto {
    private int room_id;
    private String room_name;
    private String last_message;
    private LocalDateTime last_time;
    private Integer unread_count;
}
