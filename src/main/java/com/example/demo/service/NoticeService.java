package com.example.demo.service;

import com.example.demo.dto.NoticeDto;
import com.example.demo.mapper.NoticeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class NoticeService {
    private final NoticeMapper noticeMapper;

    public void insertNotice(NoticeDto notice) {
        noticeMapper.insertNotice(notice);
    }
        public Map<String, Object> getNoticeList (int page, String keyword){
            int size = 10;
            int offset = (page - 1) * size;

            Map<String, Object> param = new HashMap<>();
            param.put("keyword", keyword == null ? "" : keyword);
            param.put("offset", offset);
            param.put("size", size);

            List<NoticeDto> list = noticeMapper.selectNoticeList(param);
            int total = noticeMapper.selectNoticeCount(param);

            Map<String, Object> result = new HashMap<>();
            result.put("list", list);
            result.put("total", total);
            result.put("page", page);
            result.put("size", size);

            return result;
        }
        @Transactional
        public NoticeDto getNoteceDetail(Long id) {
            noticeMapper.increaseViewCount(id);
            return noticeMapper.selectNoteicDetail(id);


    }

}


