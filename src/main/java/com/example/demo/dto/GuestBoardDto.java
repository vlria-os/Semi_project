package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class GuestBoardDto {
    private int id;
    private String name;
    private String password;
    private String title;
    private String content;
    private Date createdAt;
    private int viewCount;
    private String category;

}
