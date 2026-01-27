package com.example.demo.mapper;

import com.example.demo.dto.NoticeDto;
import com.example.demo.dto.NoticeReplyDto;
import com.example.demo.pagination.PageInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface NoticeMapper {
        List<NoticeDto> selectNoticeList(
                        @Param("pageInfo") PageInfo pageInfo,
                        @Param("startDate") String startDate,
                        @Param("keyword") String keyword,
                        @Param("endDate") String endDate);

        int insertNotice(NoticeDto notice);

        NoticeDto selectNoticeDetail(Long noticeId);

        void increaseViewCount(Long noticeId);

        int updateNotice(NoticeDto notice);

        int deleteNotice(Long noticeId);

        List<NoticeDto> selectPinnedNotices();

        List<NoticeReplyDto> selectRepliesbyNoticeId(Long noticeId);

        int insertReply(NoticeReplyDto dto);

        int updateReply(NoticeReplyDto replyDto);
        int deleteReply(Long replyId);

        int count(String keyword);
}
