package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ChatRoomDTO {
    private int room_id;
    private String room_type;
    private String room_name;
    private LocalDateTime created_at;
}
