package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class NoticeReplyDto {
    private Long replyId;
    private Long noticeId;
    private Integer webuserId;
    private String content;
    private LocalDateTime createdAt;
    private String webuserName;
    private Long parentReplyId;
    private int replyCount;
}
