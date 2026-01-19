package com.example.demo.mapper;

import com.example.demo.dto.NoticeDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface NoticeMapper {
    List<NoticeDto> selectNoticeList(
            @Param("offset") int offset,
            @Param("limit") int limit,
            @Param("startDate") String startDate,
            @Param("endDate") String endDate
    );

    int insertNotice(NoticeDto notice);

    NoticeDto selectNoticeDetail(Long noticeId);

    void increaseViewCount(Long noticeId);

    void updateNotice(NoticeDto notice);

    void deleteNotice(Long noticeId);

    List<NoticeDto>selectPinnedNotices();

    }

