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
                        @Param("endDate") String endDate,
                        @Param("keyword") String keyword);

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

        int count(String startDate, String endDate, String keyword);
        int countChildReplies (Long id);

        void deleteRepliesByNoticeId(Long id);
}
