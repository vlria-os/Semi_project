package com.example.demo.service;

import com.example.demo.dto.NoticeDto;
import com.example.demo.mapper.NoticeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeMapper noticeMapper;

    public Map<String, Object> getNoticeList(
            int page, String startDate, String endDate) {

        int limit = 10;
        int offset = (page - 1) * limit;

        List<NoticeDto> list = noticeMapper.selectNoticeList(offset, limit, startDate, endDate);

        boolean hasNext = list.size() == limit;

        Map<String, Object> map = new HashMap<>();
        map.put("list", list);
        map.put("hasNext", hasNext);
        return map;
    }

    // 상단 고정 공지 조회
    public List<NoticeDto> getPinnedNotices() {
        return noticeMapper.selectPinnedNotices();
    }

    // 등록
    public int saveNotice(NoticeDto notice) {
        return noticeMapper.insertNotice(notice);
    }

    // 상세
    public NoticeDto getNoticeDetail(Long noticeId) {
        noticeMapper.increaseViewCount(noticeId);
        return noticeMapper.selectNoticeDetail(noticeId);
    }

    // 수정
    public int updateNotice(NoticeDto notice) {
        return noticeMapper.updateNotice(notice);
    }

    // 삭제
    public int deleteNotice(Long noticeId) {
        return noticeMapper.deleteNotice(noticeId);
    }
}