package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ChatInviteResultDto {
    private boolean ok;
    private List<Integer> invitedUserIds;
    private String message;
}
