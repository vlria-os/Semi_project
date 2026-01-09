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
    private Integer viewcount;// 조회수
    private String pinYn; // 주요공지
    private LocalDateTime createdAt;  // 가입일


}
