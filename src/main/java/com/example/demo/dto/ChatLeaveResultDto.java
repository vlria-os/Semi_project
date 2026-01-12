package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ChatLeaveResultDto {
    private int roomId;
    private int leaverId;
    private List<Integer> remainingUserIds;
}
