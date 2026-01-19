package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class NoticeDto {
    private Long noticeId; // 글번호
    private String title;  // 제목
    private String  content;// 내용
    private String writer;// 작성자
    private Integer viewCount;// 조회수
    private String pinYn= "N"; // 주요공지
    private LocalDateTime createdAt;  // 가입일
    private LocalDateTime updatedAt;  // 수정일

}
