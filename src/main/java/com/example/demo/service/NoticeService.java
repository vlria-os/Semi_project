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

    public Map<String, Object> getNoticeList(
            int page, String startDate, String endDate) {

        int limit = 10;
        int offset = (page - 1) * limit;

        List<NoticeDto> list =
                noticeMapper.selectNoticeList(offset, limit, startDate, endDate);

        boolean hasNext = list.size() == limit;

        Map<String, Object> map = new HashMap<>();
        map.put("list", list);
        map.put("hasNext", hasNext);

        return map;
    }

    public void saveNotice(NoticeDto notice) {
        noticeMapper.insertNotice(notice);
    }

    public NoticeDto getNoticeDetail(Long noticeId) {
        noticeMapper.increaseViewCount(noticeId);
        return noticeMapper.selectNoticeDetail(noticeId);
    }
    public void updateNotice(NoticeDto notice) {
        noticeMapper.updateNotice(notice);
    }
}