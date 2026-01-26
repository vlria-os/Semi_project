package com.example.demo.dto;

import lombok.Data;

import java.util.Date;

@Data
public class GuestCommentDto {
    private int id;
    private int boardId; //댓글이 속한 게시글 ID
    private String name;
    private String password;
    private String content;
    private Date createAt;

}
