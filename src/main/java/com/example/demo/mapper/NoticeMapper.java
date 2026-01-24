package com.example.demo.mapper;

import com.example.demo.dto.NoticeDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface NoticeMapper {
        List<NoticeDto> selectNoticeList(
                        @Param("offset") int offset,
                        @Param("limit") int limit,
                        @Param("startDate") String startDate,
                        @Param("endDate") String endDate);

        int insertNotice(NoticeDto notice);

        NoticeDto selectNoticeDetail(Long noticeId);

        void increaseViewCount(Long noticeId);

        int updateNotice(NoticeDto notice);

        int deleteNotice(Long noticeId);

        List<NoticeDto> selectPinnedNotices();

}
