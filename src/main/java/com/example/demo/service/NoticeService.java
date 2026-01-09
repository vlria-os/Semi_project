package com.example.demo.service;

import com.example.demo.dto.NoticeDto;
import com.example.demo.mapper.NoticeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NoticeService {
    private final NoticeMapper noticeMapper;

    public void insertNotice(NoticeDto notice){
        noticeMapper.insertNotice(notice);

    }

}
